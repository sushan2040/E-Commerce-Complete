#!/bin/bash
echo "Installing dependencies..."
yum update -y
yum install -y java-11-openjdk

# Install necessary dependencies
echo "Updating system packages..."
sudo yum update -y || sudo apt-get update -y

# Install Ruby and wget (required for CodeDeploy Agent)
echo "Installing Ruby and wget..."
if [ -f /etc/redhat-release ]; then
  sudo yum install -y ruby wget
elif [ -f /etc/lsb-release ]; then
  sudo apt-get install -y ruby wget
fi

# Check if CodeDeploy agent is already installed
if ! service codedeploy-agent status &>/dev/null; then
  echo "Installing AWS CodeDeploy agent..."
  cd /tmp
  wget https://aws-codedeploy-REGION.s3.REGION.amazonaws.com/latest/install
  chmod +x ./install
  sudo ./install auto
  sudo service codedeploy-agent start
  sudo systemctl enable codedeploy-agent
else
  echo "CodeDeploy agent is already installed."
fi

# Verify CodeDeploy agent status
if service codedeploy-agent status &>/dev/null; then
  echo "CodeDeploy agent is running."
else
  echo "Failed to start CodeDeploy agent." >&2
  exit 1
fi
