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

# Install uv (fast Python package installer)
RUN curl -LsSf https://astral.sh/uv/install.sh | sh
ENV PATH="/root/.cargo/bin:$PATH"

# Set working directory
WORKDIR /app

# Copy project structure
COPY . .

# Build Java dependencies (cache Gradle wrapper and dependencies)
WORKDIR /app/java
RUN ./gradlew build --no-daemon --exclude-task test || true
RUN ./gradlew dependencies --no-daemon || true

# Build Python dependencies
WORKDIR /app/python
RUN uv sync --frozen

# Stage 2: Runtime environment
FROM eclipse-temurin:11-jdk-jammy AS runtime

# Install Python 3.11 and minimal runtime dependencies
RUN apt-get update && apt-get install -y \
    python3.11 \
    python3.11-venv \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Install uv for runtime
RUN curl -LsSf https://astral.sh/uv/install.sh | sh
ENV PATH="/root/.cargo/bin:$PATH"

# Create non-root user for security
RUN useradd -m -u 1000 testrunner && \
    mkdir -p /app /app/reports /app/logs && \
    chown -R testrunner:testrunner /app

# Switch to non-root user
USER testrunner
WORKDIR /app

# Copy built project from builder stage
COPY --from=builder --chown=testrunner:testrunner /app .

# Copy Gradle wrapper and cached dependencies
COPY --from=builder --chown=testrunner:testrunner /root/.gradle /home/testrunner/.gradle

# Set up Python virtual environment
WORKDIR /app/python
RUN uv sync --frozen

# Set environment variables
ENV PYTHONUNBUFFERED=1
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false"
ENV PATH="/app/python/.venv/bin:$PATH"

# Create entrypoint script
WORKDIR /app
RUN echo '#!/bin/bash\nset -e\nif [ "$#" -eq 0 ]; then\n    python3.11 scripts/run_tests.py --help\nelse\n    python3.11 scripts/run_tests.py "$@"\nfi' > /app/entrypoint.sh && \
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