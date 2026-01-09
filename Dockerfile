# Multi-stage Dockerfile for Appium Test Automation
# Stage 1: Build environment with all tools
FROM eclipse-temurin:11-jdk-jammy AS builder

# Install Python 3.11 and system dependencies
RUN apt-get update && apt-get install -y \
    python3.11 \
    python3.11-dev \
    python3.11-venv \
    python3-pip \
    curl \
    unzip \
    git \
    && rm -rf /var/lib/apt/lists/*

# Install uv system-wide so it's accessible to all users
RUN curl -LsSf https://astral.sh/uv/install.sh | sh && \
    mv /root/.local/bin/uv /usr/local/bin/uv && \
    chmod +x /usr/local/bin/uv

# Verify uv installation
RUN uv --version

# Set working directory
WORKDIR /app

# Copy project structure
COPY . .

# Build Java dependencies (cache Gradle wrapper and dependencies)
WORKDIR /app/java
# Symlink to root gradle wrapper should already exist from COPY, but create if missing
RUN test -L gradle || ln -s /app/gradle gradle
RUN ./gradlew compileTestJava --no-daemon || true
RUN ./gradlew dependencies --no-daemon || true

# Ensure .gradle directory exists for copying to runtime stage
RUN mkdir -p /root/.gradle

# Build Python dependencies
WORKDIR /app/python
RUN uv sync

# Stage 2: Runtime environment
FROM eclipse-temurin:11-jdk-jammy AS runtime

# Install Python 3.11 and minimal runtime dependencies
RUN apt-get update && apt-get install -y \
    python3.11 \
    python3.11-venv \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Install uv system-wide for runtime
RUN curl -LsSf https://astral.sh/uv/install.sh | sh && \
    mv /root/.local/bin/uv /usr/local/bin/uv && \
    chmod +x /usr/local/bin/uv

# Create non-root user for security
RUN useradd -m -u 1000 testrunner && \
    mkdir -p /app /app/reports /app/logs && \
    chown -R testrunner:testrunner /app

# Set working directory before copying
WORKDIR /app

# Copy built project from builder stage
COPY --from=builder --chown=testrunner:testrunner /app .

# Copy Gradle wrapper and cached dependencies
COPY --from=builder --chown=testrunner:testrunner /root/.gradle /home/testrunner/.gradle

# Ensure gradlew has execute permissions (if it exists)
RUN if [ -f /app/java/gradlew ]; then chmod +x /app/java/gradlew; else echo "Warning: gradlew not found"; fi

# Switch to non-root user
USER testrunner

# Set environment variables
ENV PYTHONUNBUFFERED=1
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false"
ENV PATH="/app/python/.venv/bin:$PATH"

# Create entrypoint script
RUN echo '#!/bin/bash\nset -e\nif [ "$#" -eq 0 ]; then\n    python3.11 /app/scripts/run_tests.py --help\nelse\n    python3.11 /app/scripts/run_tests.py "$@"\nfi' > /app/entrypoint.sh && \
    chmod +x /app/entrypoint.sh

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD python3.11 -c "import sys; sys.exit(0)" || exit 1

# Expose volume for reports and logs
VOLUME ["/app/reports", "/app/logs"]

# Set entrypoint
ENTRYPOINT ["/app/entrypoint.sh"]

# Default command shows help
CMD ["--help"]