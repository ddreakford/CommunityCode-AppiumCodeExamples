#!/usr/bin/env python3
"""
Appium Test Runner
Designed for execution within a container; but can be run standalone
Supports parallel execution of Java/TestNG and Python/pytest tests
- Java tests are run via TestNG
- Python tests are run via pytest
"""

import argparse
import os
import sys
import subprocess
import threading
import time
import signal
from concurrent.futures import ThreadPoolExecutor, as_completed
from pathlib import Path
from typing import List, Dict, Optional, Tuple
import json

class TestRunner:
    def __init__(self):
        self.base_dir = Path(__file__).parent.parent
        self.java_dir = self.base_dir / "java"
        self.python_dir = self.base_dir / "python"
        self.reports_dir = self.base_dir / "reports"
        self.logs_dir = self.base_dir / "logs"
        self.shutdown_event = threading.Event()
        
        # Ensure directories exist
        self.reports_dir.mkdir(exist_ok=True)
        self.logs_dir.mkdir(exist_ok=True)
        
        # Set up signal handlers for graceful shutdown
        signal.signal(signal.SIGINT, self._signal_handler)
        signal.signal(signal.SIGTERM, self._signal_handler)

    def _signal_handler(self, signum, frame):
        print(f"\n📋 Received signal {signum}, initiating graceful shutdown...")
        self.shutdown_event.set()

    def validate_environment(self) -> bool:
        """Validate required environment variables and project structure"""
        print("🔍 Validating environment...")
        
        # Check required directories
        required_dirs = [self.java_dir, self.python_dir]
        for dir_path in required_dirs:
            if not dir_path.exists():
                print(f"❌ Required directory not found: {dir_path}")
                return False
        
        # Check environment variables (warn if missing, don't fail)
        env_vars = ["CLOUD_URL", "ACCESS_KEY", "APPIUM_VERSION"]
        missing_vars = []
        for var in env_vars:
            if not os.getenv(var):
                missing_vars.append(var)
        
        if missing_vars:
            print(f"⚠️  Warning: Missing environment variables: {', '.join(missing_vars)}")
            print("   Tests will use default values or may fail if cloud connectivity is required")
        
        print("✅ Environment validation complete")
        return True

    def run_java_tests(self, test_suites: Optional[str] = "testng.xml", test_filter: Optional[str] = None, fork_count: int = 4) -> Tuple[bool, str]:
        """Run Java/TestNG tests using Gradle"""
        print("☕ Starting Java/TestNG tests...")
        
        cmd = ["./gradlew", "test", "--no-daemon"]

        # Parallel execution
        #
        # Gradle/JVM level:
        #     maxParallelForks (read via build.gradle)
        # TestNG level:
        #     testng.parallel=methods
        #     test.ng.thread-count (not used here)
        cmd.extend([
            f"-PmaxForks={fork_count}",
            f"-Dtestng.parallel=methods"
        ])
        
        # HTML reports
        cmd.extend(["-Dtest.html.report=true"])

        # TestNG suites
        cmd.extend([f"-Psuites={test_suites}"])
        
        # Test filtering
        #
        # Well-known suites (for example, QuickStart) have built-in params.
        #
        # For others, test names (as specified in a suite XML file) can be
        # directly passed via the `--tests` parameter.
        if test_filter:
            if test_filter == "quickstart":
                cmd.extend(["--tests", "*QuickStart*"])
            elif test_filter == "advanced":
                cmd.extend(["--tests", "*AdvancedCommands*"])
            elif test_filter == "optional":
                cmd.extend(["--tests", "*OptionalCapabilities*"])
            elif test_filter == "android":
                cmd.extend(["--tests", "*Android*"])
            elif test_filter == "ios":
                cmd.extend(["--tests", "*IOS*"])
            else:
                cmd.extend(["--tests", f"*{test_filter}*"])
                
        log_file = self.logs_dir / f"java_tests_{int(time.time())}.log"
        
        try:
            with open(log_file, "w") as f:
                process = subprocess.Popen(
                    cmd,
                    cwd=self.java_dir,
                    stdout=subprocess.PIPE,
                    stderr=subprocess.STDOUT,
                    text=True,
                    bufsize=1,
                    universal_newlines=True
                )
                
                # Stream output in real-time
                for line in process.stdout:
                    if self.shutdown_event.is_set():
                        process.terminate()
                        break
                    print(f"☕ {line.rstrip()}")
                    f.write(line)
                
                return_code = process.wait()
                success = return_code == 0
                
                # Copy HTML reports
                java_reports = self.java_dir / "build" / "reports" / "tests" / "test"
                if java_reports.exists():
                    import shutil
                    target_dir = self.reports_dir / "java"
                    if target_dir.exists():
                        shutil.rmtree(target_dir)
                    shutil.copytree(java_reports, target_dir)
                
                return success, str(log_file)
                
        except Exception as e:
            error_msg = f"Java test execution failed: {str(e)}"
            print(f"❌ {error_msg}")
            return False, error_msg

    def run_python_tests(self, test_filter: Optional[str] = None) -> Tuple[bool, str]:
        """Run Python/pytest tests using uv"""
        print("🐍 Starting Python/pytest tests...")
        
        cmd = ["uv", "run", "pytest", "-v", "--tb=short"]
        
        # Add HTML report generation
        html_report = self.reports_dir / "python" / "report.html"
        html_report.parent.mkdir(exist_ok=True)
        cmd.extend(["--html", str(html_report), "--self-contained-html"])
        
        # Add test filtering if specified
        if test_filter:
            if test_filter == "quickstart":
                cmd.extend(["-k", "quick_start"])
            elif test_filter == "android":
                cmd.extend(["-m", "android"])
            elif test_filter == "ios":
                cmd.extend(["-m", "ios"])
            else:
                cmd.extend(["-k", test_filter])
        
        log_file = self.logs_dir / f"python_tests_{int(time.time())}.log"
        
        try:
            with open(log_file, "w") as f:
                process = subprocess.Popen(
                    cmd,
                    cwd=self.python_dir,
                    stdout=subprocess.PIPE,
                    stderr=subprocess.STDOUT,
                    text=True,
                    bufsize=1,
                    universal_newlines=True
                )
                
                # Stream output in real-time
                for line in process.stdout:
                    if self.shutdown_event.is_set():
                        process.terminate()
                        break
                    print(f"🐍 {line.rstrip()}")
                    f.write(line)
                
                return_code = process.wait()
                success = return_code == 0
                
                return success, str(log_file)
                
        except Exception as e:
            error_msg = f"Python test execution failed: {str(e)}"
            print(f"❌ {error_msg}")
            return False, error_msg

    def run_tests_parallel(self, test_specs: List[Dict], max_workers: int = 4) -> Dict:
        """Run tests in parallel using ThreadPoolExecutor"""
        print(f"🚀 Starting parallel test execution with {max_workers} workers...")
        
        results = {
            "total_tests": len(test_specs),
            "passed": 0,
            "failed": 0,
            "results": []
        }
        
        with ThreadPoolExecutor(max_workers=max_workers) as executor:
            # Submit all test tasks
            future_to_spec = {}
            for spec in test_specs:
                if spec["type"] == "java":
                    future = executor.submit(self.run_java_tests, spec.get("suites"), spec.get("filter"), max_workers)
                else:  # python
                    future = executor.submit(self.run_python_tests, spec.get("filter"), max_workers)
                future_to_spec[future] = spec
            
            # Process completed tasks
            for future in as_completed(future_to_spec):
                if self.shutdown_event.is_set():
                    print("🛑 Shutdown requested, cancelling remaining tasks...")
                    executor.shutdown(wait=False)
                    break
                
                spec = future_to_spec[future]
                try:
                    success, log_info = future.result()
                    result = {
                        "spec": spec,
                        "success": success,
                        "log": log_info,
                        "timestamp": time.time()
                    }
                    results["results"].append(result)
                    
                    if success:
                        results["passed"] += 1
                        print(f"✅ {spec['type']} tests completed successfully")
                    else:
                        results["failed"] += 1
                        print(f"❌ {spec['type']} tests failed")
                        
                except Exception as e:
                    print(f"❌ Unexpected error in {spec['type']} tests: {str(e)}")
                    results["failed"] += 1
                    results["results"].append({
                        "spec": spec,
                        "success": False,
                        "log": str(e),
                        "timestamp": time.time()
                    })
        
        return results

    def generate_summary_report(self, results: Dict):
        """Generate a comprehensive test summary report"""
        print("\n📊 Generating test summary report...")
        
        summary = {
            "execution_time": time.strftime("%Y-%m-%d %H:%M:%S"),
            "total_tests": results["total_tests"],
            "passed": results["passed"],
            "failed": results["failed"],
            "success_rate": (results["passed"] / results["total_tests"] * 100) if results["total_tests"] > 0 else 0,
            "results": results["results"]
        }
        
        # Save JSON summary
        summary_file = self.reports_dir / "test_summary.json"
        with open(summary_file, "w") as f:
            json.dump(summary, f, indent=2)
        
        # Generate HTML summary
        html_summary = self._generate_html_summary(summary)
        html_file = self.reports_dir / "test_summary.html"
        with open(html_file, "w") as f:
            f.write(html_summary)
        
        # Print summary to console
        print(f"\n{'='*60}")
        print("📋 TEST SUITES EXECUTION SUMMARY")
        print(f"{'='*60}")
        print(f"Test Suites: {summary['total_tests']}")
        print(f"Suites Passed: {summary['passed']} ✅")
        print(f"Suites Failed: {summary['failed']} ❌")
        print(f"Suite Success Rate: {summary['success_rate']:.1f}%")
        print(f"Test Case Reports: {self.reports_dir}")
        print(f"Test Run Logs: {self.logs_dir}")
        print(f"{'='*60}")
        
        return summary_file

    def _generate_html_summary(self, summary: Dict) -> str:
        """Generate HTML summary report"""
        html = f"""
        <!DOCTYPE html>
        <html>
        <head>
            <title>Appium Test Summary</title>
            <style>
                body {{ font-family: Arial, sans-serif; margin: 40px; }}
                .header {{ background: #f5f5f5; padding: 20px; border-radius: 5px; }}
                .stats {{ display: flex; gap: 20px; margin: 20px 0; }}
                .stat {{ padding: 15px; border-radius: 5px; text-align: center; min-width: 100px; }}
                .passed {{ background: #d4edda; color: #155724; }}
                .failed {{ background: #f8d7da; color: #721c24; }}
                .total {{ background: #d1ecf1; color: #0c5460; }}
                .results {{ margin-top: 30px; }}
                .result {{ margin: 10px 0; padding: 15px; border-radius: 5px; }}
                .success {{ background: #d4edda; }}
                .failure {{ background: #f8d7da; }}
            </style>
        </head>
        <body>
            <div class="header">
                <h1>🧪 Appium Test Execution Summary</h1>
                <p>Execution Time: {summary['execution_time']}</p>
            </div>
            
            <div class="stats">
                <div class="stat total">
                    <h3>{summary['total_tests']}</h3>
                    <p>Total Test Suites</p>
                </div>
                <div class="stat passed">
                    <h3>{summary['passed']}</h3>
                    <p>Passed</p>
                </div>
                <div class="stat failed">
                    <h3>{summary['failed']}</h3>
                    <p>Failed</p>
                </div>
            </div>
            
            <div class="results">
                <h2>Test Results</h2>
        """
        
        for result in summary['results']:
            status_class = "success" if result['success'] else "failure"
            status_icon = "✅" if result['success'] else "❌"
            html += f"""
                <div class="result {status_class}">
                    <h3>{status_icon} {result['spec']['type'].title()} Tests</h3>
                    <p>Filter: {result['spec'].get('filter', 'All')}</p>
                    <p>Status: {'Passed' if result['success'] else 'Failed'}</p>
                    <p>Log: {result['log']}</p>
                </div>
            """
        
        html += """
            </div>
        </body>
        </html>
        """
        return html


def parse_arguments():
    """Parse command line arguments"""
    parser = argparse.ArgumentParser(
        prog="docker run --rm --env-file .env -v $(pwd)/reports:/app/reports appium-code-examples",
        description="""
🐳 Appium Test Runner - Containerized Test Execution

This tool is designed to run inside a Docker container with support for:
• Parallel test execution with configurable workers
• Java/TestNG and Python/pytest test suites  
• Environment variable configuration
• HTML report generation and test result aggregation
• Digital.ai Testing Cloud integration
        """.strip(),
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Container Usage Examples:
  docker run --rm --env-file .env -v $(pwd)/reports:/app/reports appium-code-examples --all --parallel=4
    # Run all tests with 4 parallel workers

  docker run --rm --env-file .env -v $(pwd)/reports:/app/reports appium-code-examples --java --tests=quickstart
    # Run Java quick start tests only

  docker run --rm --env-file .env -v $(pwd)/reports:/app/reports appium-code-examples --python --platform=android
    # Run Python Android tests only

  docker-compose run --rm appium-tests --all --parallel=2
    # Run with Docker Compose (recommended)

Local Script Usage (if running outside container):
  python scripts/run_tests.py --all --parallel=4
        """
    )
    
    # Test framework selection
    parser.add_argument("--all", action="store_true", help="Run all tests (Java and Python)")
    parser.add_argument("--java", action="store_true", help="Run Java/TestNG tests")
    parser.add_argument("--python", action="store_true", help="Run Python/pytest tests")

    # Test suite specification
    parser.add_argument("--suites", help="Suite XML files (default: testng.xml)")
    
    # Test filtering
    parser.add_argument("--tests", help="Test filter (quickstart, advanced, optional or 'test_name's as specified in a suite XML file)")
    parser.add_argument("--platform", help="Platform filter (android, ios)")
    
    # Execution options
    parser.add_argument("--parallel", type=int, default=4, 
                       help="Number of parallel test processes (default: 4)")
    parser.add_argument("--generate-reports-only", action="store_true",
                       help="Generate reports only, don't run tests")
    
    return parser.parse_args()


def main():
    """Main entry point"""
    args = parse_arguments()
    runner = TestRunner()
    
    # Handle reports-only mode
    if args.generate_reports_only:
        print("📊 Generating reports from existing results...")
        # This would need to be implemented based on stored results
        return 0
    
    # Validate environment
    if not runner.validate_environment():
        return 1
    
    # Determine test specifications
    test_specs = []
    
    if args.all:
        test_specs.extend([
            {"type": "java", "suites": args.suites or "testng.xml", "filter": args.tests or args.platform},
            {"type": "python", "filter": args.tests or args.platform}
        ])
    else:
        if args.java:
            test_specs.append({"type": "java", "suites": args.suites or "testng.xml", "filter": args.tests or args.platform})
        if args.python:
            test_specs.append({"type": "python", "filter": args.tests or args.platform})
    
    # Default to all tests if nothing specified
    if not test_specs:
        test_specs = [
            {"type": "java", "suites": None, "filter": None},
            {"type": "python", "filter": None}
        ]
    
    print(f"🎯 Test execution plan: {len(test_specs)} test suite(s)")
    for i, spec in enumerate(test_specs, 1):
        print(f"  {i}. {spec['type'].title()} tests" + 
              (f" (suites: {spec['suites']})" if spec['suites'] else "") +
              (f" (filter: {spec['filter']})" if spec['filter'] else ""))
    
    # Execute tests
    try:
        start_time = time.time()
        results = runner.run_tests_parallel(test_specs, args.parallel)
        execution_time = time.time() - start_time
        
        print(f"\n⏱️  Total execution time: {execution_time:.2f} seconds")
        
        # Generate reports
        runner.generate_summary_report(results)
        
        # Return appropriate exit code
        return 0 if results["failed"] == 0 else 1
        
    except KeyboardInterrupt:
        print("\n🛑 Test execution interrupted by user")
        return 130
    except Exception as e:
        print(f"\n💥 Unexpected error: {str(e)}")
        return 1


if __name__ == "__main__":
    sys.exit(main())