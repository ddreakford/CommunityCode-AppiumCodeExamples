#!/usr/bin/env python3
"""
Generate architectural and sequence diagrams for Appium Samples In a Box
"""

import matplotlib.pyplot as plt
import matplotlib.patches as patches
from matplotlib.patches import FancyBboxPatch, ConnectionPatch
import numpy as np

def create_architectural_diagram():
    """Create system architecture diagram"""
    fig, ax = plt.subplots(1, 1, figsize=(14, 10))
    ax.set_xlim(0, 14)
    ax.set_ylim(0, 10)
    ax.axis('off')
    
    # Title
    ax.text(7, 9.5, 'Appium Samples In a Box Architecture', 
            fontsize=16, fontweight='bold', ha='center')
    
    # Color scheme
    colors = {
        'test_runner': '#E3F2FD',
        'container': '#FFF3E0', 
        'build_tools': '#F3E5F5',
        'cloud': '#E8F5E8'
    }
    
    # Docker Container (larger to contain Test Runner)
    container = FancyBboxPatch((4, 3.5), 6, 5.5, 
                              boxstyle="round,pad=0.1", 
                              facecolor=colors['container'],
                              edgecolor='#F57C00', linewidth=2)
    ax.add_patch(container)
    ax.text(7, 8.5, 'Docker Container\n(Test Execution Environment)', 
            ha='center', va='center', fontweight='bold', fontsize=11)
    
    # Test Runner (inside Docker Container)
    test_runner = FancyBboxPatch((4.5, 7.5), 2.5, 1, 
                                boxstyle="round,pad=0.1", 
                                facecolor=colors['test_runner'],
                                edgecolor='#1976D2', linewidth=2)
    ax.add_patch(test_runner)
    ax.text(5.75, 8, 'Test Runner\n(scripts/run_tests.py)', 
            ha='center', va='center', fontweight='bold', fontsize=9)
    
    # Build Tools inside container
    gradle_box = FancyBboxPatch((4.5, 6.2), 1.3, 0.8, 
                               boxstyle="round,pad=0.05", 
                               facecolor=colors['build_tools'],
                               edgecolor='#7B1FA2', linewidth=1)
    ax.add_patch(gradle_box)
    ax.text(5.15, 6.6, 'Gradle\n(Java/TestNG)', 
            ha='center', va='center', fontsize=9)
    
    uv_box = FancyBboxPatch((7.2, 6.2), 1.3, 0.8, 
                           boxstyle="round,pad=0.05", 
                           facecolor=colors['build_tools'],
                           edgecolor='#7B1FA2', linewidth=1)
    ax.add_patch(uv_box)
    ax.text(7.85, 6.6, 'uv\n(Python/pytest)', 
            ha='center', va='center', fontsize=9)
    
    # Test execution area inside container
    test_area = FancyBboxPatch((4.5, 4.5), 4.5, 1.2, 
                              boxstyle="round,pad=0.05", 
                              facecolor='#FFFFFF',
                              edgecolor='#666666', linewidth=1, linestyle='--')
    ax.add_patch(test_area)
    ax.text(6.75, 5.1, 'Parallel Test Execution\n• Java/TestNG Tests\n• Python/pytest Tests', 
            ha='center', va='center', fontsize=9)
    
    # DAI Testing Cloud
    cloud = FancyBboxPatch((10.5, 6), 3, 2, 
                          boxstyle="round,pad=0.1", 
                          facecolor=colors['cloud'],
                          edgecolor='#388E3C', linewidth=2)
    ax.add_patch(cloud)
    ax.text(12, 7, 'Digital.ai Testing Cloud\n(Remote Platform)\n\nCLOUD_URL:\nhttps://uscloud.experitest.com', 
            ha='center', va='center', fontweight='bold', fontsize=10)
    
    # User/Developer box
    user_box = FancyBboxPatch((0.5, 7.5), 2.5, 1, 
                             boxstyle="round,pad=0.1", 
                             facecolor='#E8F5E8',
                             edgecolor='#388E3C', linewidth=2)
    ax.add_patch(user_box)
    ax.text(1.75, 8, 'User/Developer\n(Command Execution)', 
            ha='center', va='center', fontweight='bold', fontsize=10)
    
    # Reports & Logs
    reports_box = FancyBboxPatch((0.5, 3), 3, 1.5, 
                                boxstyle="round,pad=0.1", 
                                facecolor='#FFF8E1',
                                edgecolor='#F9A825', linewidth=2)
    ax.add_patch(reports_box)
    ax.text(2, 3.75, 'Reports & Logs\n• HTML Reports\n• JSON Summary\n• Test Logs', 
            ha='center', va='center', fontweight='bold', fontsize=10)
    
    # Environment Configuration
    env_box = FancyBboxPatch((0.5, 0.5), 3, 1.5, 
                            boxstyle="round,pad=0.1", 
                            facecolor='#FCE4EC',
                            edgecolor='#C2185B', linewidth=2)
    ax.add_patch(env_box)
    ax.text(2, 1.25, 'Environment Config\n• .env file\n• CLOUD_URL\n• ACCESS_KEY\n• APPIUM_VERSION', 
            ha='center', va='center', fontweight='bold', fontsize=9)
    
    # Arrows and connections
    # User to Docker Container
    arrow1 = ConnectionPatch((3.5, 8), (4, 8), "data", "data",
                           arrowstyle="->", shrinkA=5, shrinkB=5, 
                           mutation_scale=20, fc="black")
    ax.add_artist(arrow1)
    ax.text(3.75, 8.3, 'Execute\nCommand', ha='center', va='center', fontsize=8, 
            bbox=dict(boxstyle="round,pad=0.3", facecolor='white', alpha=0.8))
    
    # Container to Cloud
    arrow2 = ConnectionPatch((10, 7), (10.5, 7), "data", "data",
                           arrowstyle="->", shrinkA=5, shrinkB=5, 
                           mutation_scale=20, fc="green")
    ax.add_artist(arrow2)
    ax.text(10.25, 7.5, 'API\nCalls', ha='center', va='center', fontsize=8,
            bbox=dict(boxstyle="round,pad=0.3", facecolor='white', alpha=0.8))
    
    # Container to Reports
    arrow3 = ConnectionPatch((4, 5.5), (3.5, 4), "data", "data",
                           arrowstyle="->", shrinkA=5, shrinkB=5, 
                           mutation_scale=20, fc="orange")
    ax.add_artist(arrow3)
    ax.text(3.5, 4.5, 'Generate\nReports', ha='center', va='center', fontsize=8,
            bbox=dict(boxstyle="round,pad=0.3", facecolor='white', alpha=0.8))
    
    # Environment to Container
    arrow4 = ConnectionPatch((3.5, 1.25), (4.5, 4.5), "data", "data",
                           arrowstyle="->", shrinkA=5, shrinkB=5, 
                           mutation_scale=20, fc="purple")
    ax.add_artist(arrow4)
    ax.text(3.5, 2.8, 'Config\nMount', ha='center', va='center', fontsize=8,
            bbox=dict(boxstyle="round,pad=0.3", facecolor='white', alpha=0.8))
    
    plt.tight_layout()
    return fig

def create_sequence_diagram():
    """Create sequence diagram for test execution flow"""
    fig, ax = plt.subplots(1, 1, figsize=(14, 10))
    ax.set_xlim(0, 14)
    ax.set_ylim(0, 10)
    ax.axis('off')
    
    # Title
    ax.text(7, 9.5, 'Appium Samples In a Box Sequence Flow', 
            fontsize=16, fontweight='bold', ha='center')
    
    # Actors/Components
    actors = [
        ('User', 1),
        ('Docker', 4),
        ('Environment\nValidation', 6.5),
        ('Build Tools', 9),
        ('DAI Cloud', 11.5)
    ]
    
    # Draw actor boxes and lifelines
    for actor, x in actors:
        # Actor box
        actor_box = FancyBboxPatch((x-0.7, 8.5), 1.4, 0.6, 
                                  boxstyle="round,pad=0.05", 
                                  facecolor='#E1F5FE',
                                  edgecolor='#0277BD', linewidth=1)
        ax.add_patch(actor_box)
        ax.text(x, 8.8, actor, ha='center', va='center', fontweight='bold', fontsize=10)
        
        # Lifeline
        ax.plot([x, x], [8.5, 0.5], 'k--', alpha=0.5, linewidth=1)
    
    # Sequence steps
    steps = [
        (8.0, 1, 4, "1. User Command\n(docker run --all --parallel=4)", 'right'),
        (7.5, 4, 6.5, "2. Environment Validation\n(CLOUD_URL, ACCESS_KEY)", 'right'),
        (7.0, 4, 9, "3. Execute Build Tools\n(Gradle for Java, uv for Python)", 'right'),
        (6.5, 4, 11.5, "4. Cloud Connection\n(Connect to DAI Testing Cloud)", 'right'),
        (6.0, 11.5, 4, "5. Return device sessions", 'left'),
        (5.5, 4, 4, "6. Parallel Test Execution\n(Java/TestNG & Python/pytest)", 'self'),
        (5.0, 4, 11.5, "7. Execute tests on\nremote devices", 'right'),
        (4.5, 11.5, 4, "8. Return test results", 'left'),
        (4.0, 4, 4, "9. Generate Reports\n(HTML & JSON)", 'self'),
        (3.5, 4, 1, "10. Display summary\n& exit status", 'left'),
    ]
    
    # Draw sequence arrows and labels
    for y, from_x, to_x, label, direction in steps:
        if direction == 'self':
            # Self-call (loopback)
            ax.annotate('', xy=(from_x + 0.5, y), xytext=(from_x, y),
                       arrowprops=dict(arrowstyle='->', lw=1.5, color='blue'))
            ax.plot([from_x, from_x + 0.5, from_x + 0.5, from_x], 
                   [y, y, y - 0.1, y - 0.1], 'b-', linewidth=1.5)
            ax.text(from_x + 0.7, y - 0.05, label, fontsize=9, va='center')
        else:
            # Regular arrow
            if direction == 'right':
                ax.annotate('', xy=(to_x - 0.1, y), xytext=(from_x + 0.1, y),
                           arrowprops=dict(arrowstyle='->', lw=1.5, color='blue'))
                ax.text((from_x + to_x) / 2, y + 0.15, label, fontsize=9, 
                       ha='center', va='bottom')
            else:  # left
                ax.annotate('', xy=(to_x + 0.1, y), xytext=(from_x - 0.1, y),
                           arrowprops=dict(arrowstyle='->', lw=1.5, color='green'))
                ax.text((from_x + to_x) / 2, y + 0.15, label, fontsize=9, 
                       ha='center', va='bottom')
    
    # Add activation boxes (showing when components are active)
    activations = [
        (4, 8.0, 4.5),    # Docker
        (6.5, 7.5, 0.5),  # Environment Validation
        (9, 7.0, 1.0),    # Build Tools
        (11.5, 6.5, 2.0), # DAI Cloud
    ]
    
    for x, y_start, height in activations:
        activation_box = FancyBboxPatch((x-0.05, y_start-height), 0.1, height, 
                                       boxstyle="square,pad=0", 
                                       facecolor='yellow', alpha=0.3,
                                       edgecolor='orange', linewidth=1)
        ax.add_patch(activation_box)
    
    # Add notes
    note_box = FancyBboxPatch((9.5, 1.5), 4, 1, 
                             boxstyle="round,pad=0.1", 
                             facecolor='#FFFDE7',
                             edgecolor='#F9A825', linewidth=1)
    ax.add_patch(note_box)
    ax.text(11.5, 2, 'Notes:\n• Tests run in parallel (configurable workers)\n• Reports generated in HTML and JSON formats\n• Environment variables configure cloud connection', 
            ha='center', va='center', fontsize=9)
    
    plt.tight_layout()
    return fig

def main():
    """Generate both diagrams and save as PNG files"""
    
    # Create architectural diagram
    print("Creating architectural diagram...")
    arch_fig = create_architectural_diagram()
    arch_fig.savefig('appium_architecture.png', 
                     dpi=300, bbox_inches='tight', facecolor='white')
    print("✅ Architectural diagram saved as: appium_architecture.png")
    
    # Create sequence diagram  
    print("Creating sequence diagram...")
    seq_fig = create_sequence_diagram()
    seq_fig.savefig('appium_sequence.png', 
                    dpi=300, bbox_inches='tight', facecolor='white')
    print("✅ Sequence diagram saved as: appium_sequence.png")
    
    plt.close('all')
    
    print("\n📊 Both diagrams have been generated successfully!")
    print("Files created:")
    print("  - appium_architecture.png (System Architecture)")
    print("  - appium_sequence.png (Test Execution Flow)")

if __name__ == "__main__":
    main()