# AWS Deployment Guide

## Overview

This guide provides step-by-step instructions for deploying the Trading Sandbox application on AWS using either EC2 or ECS.

## Prerequisites

- AWS CLI configured with appropriate permissions
- Docker and Docker Compose installed locally
- Domain name (optional, for production)

## Option 1: EC2 Deployment

### Step 1: Launch EC2 Instance

1. **Launch Instance:**
   ```bash
   # Launch Ubuntu 22.04 LTS instance
   aws ec2 run-instances \
     --image-id ami-0c02fb55956c7d316 \
     --count 1 \
     --instance-type t3.medium \
     --key-name your-key-pair \
     --security-group-ids sg-xxxxxxxxx \
     --subnet-id subnet-xxxxxxxxx
   ```

2. **Security Group Configuration:**
   - HTTP (80)
   - HTTPS (443)
   - SSH (22)
   - Custom TCP (8080) - Backend API
   - Custom TCP (3000) - Frontend (if needed)

### Step 2: Connect and Setup

```bash
# Connect to instance
ssh -i your-key.pem ubuntu@your-instance-ip

# Update system
sudo apt update && sudo apt upgrade -y

# Install Docker
sudo apt install -y docker.io docker-compose
sudo usermod -a -G docker ubuntu
sudo systemctl enable docker
sudo systemctl start docker

# Logout and login again for group changes
exit
ssh -i your-key.pem ubuntu@your-instance-ip
```

### Step 3: Deploy Application

```bash
# Clone repository
git clone https://github.com/your-username/cloud-algo-trading.git
cd cloud-algo-trading

# Create environment file
cat > .env << EOF
POSTGRES_DB=trading_sandbox
POSTGRES_USER=trading_user
POSTGRES_PASSWORD=secure_password_123
JWT_SECRET=your_super_secret_jwt_key_for_trading_sandbox_2024
JWT_EXPIRATION=86400000
ALPHA_VANTAGE_API_KEY=your_api_key
EOF

# Start services
docker-compose up -d

# Check status
docker-compose ps
```

### Step 4: Configure Nginx (Optional)

```bash
# Install nginx
sudo apt install -y nginx

# Create nginx configuration
sudo tee /etc/nginx/sites-available/trading-sandbox << EOF
server {
    listen 80;
    server_name your-domain.com;

    location / {
        proxy_pass http://localhost:3000;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }

    location /api/ {
        proxy_pass http://localhost:8080/api/;
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }

    location /ws/ {
        proxy_pass http://localhost:8080/ws/;
        proxy_http_version 1.1;
        proxy_set_header Upgrade \$http_upgrade;
        proxy_set_header Connection "upgrade";
        proxy_set_header Host \$host;
        proxy_set_header X-Real-IP \$remote_addr;
        proxy_set_header X-Forwarded-For \$proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto \$scheme;
    }
}
EOF

# Enable site
sudo ln -s /etc/nginx/sites-available/trading-sandbox /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl restart nginx
```

## Option 2: ECS Deployment

### Step 1: Create ECR Repositories

```bash
# Create repositories
aws ecr create-repository --repository-name trading-sandbox-backend
aws ecr create-repository --repository-name trading-sandbox-frontend

# Get login token
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin your-account-id.dkr.ecr.us-east-1.amazonaws.com
```

### Step 2: Build and Push Images

```bash
# Build backend
cd backend
docker build -t trading-sandbox-backend .
docker tag trading-sandbox-backend:latest your-account-id.dkr.ecr.us-east-1.amazonaws.com/trading-sandbox-backend:latest
docker push your-account-id.dkr.ecr.us-east-1.amazonaws.com/trading-sandbox-backend:latest

# Build frontend
cd ../frontend
docker build -t trading-sandbox-frontend .
docker tag trading-sandbox-frontend:latest your-account-id.dkr.ecr.us-east-1.amazonaws.com/trading-sandbox-frontend:latest
docker push your-account-id.dkr.ecr.us-east-1.amazonaws.com/trading-sandbox-frontend:latest
```

### Step 3: Create ECS Cluster

```bash
# Create cluster
aws ecs create-cluster --cluster-name trading-sandbox-cluster
```

### Step 4: Create Task Definitions

Create `backend-task-definition.json`:
```json
{
  "family": "trading-sandbox-backend",
  "networkMode": "awsvpc",
  "requiresCompatibilities": ["FARGATE"],
  "cpu": "512",
  "memory": "1024",
  "executionRoleArn": "arn:aws:iam::your-account-id:role/ecsTaskExecutionRole",
  "containerDefinitions": [
    {
      "name": "backend",
      "image": "your-account-id.dkr.ecr.us-east-1.amazonaws.com/trading-sandbox-backend:latest",
      "portMappings": [
        {
          "containerPort": 8080,
          "protocol": "tcp"
        }
      ],
      "environment": [
        {
          "name": "SPRING_PROFILES_ACTIVE",
          "value": "prod"
        },
        {
          "name": "SPRING_DATASOURCE_URL",
          "value": "jdbc:postgresql://your-rds-endpoint:5432/trading_sandbox"
        }
      ],
      "secrets": [
        {
          "name": "SPRING_DATASOURCE_PASSWORD",
          "valueFrom": "arn:aws:secretsmanager:us-east-1:your-account-id:secret:trading-sandbox/db-password"
        }
      ],
      "logConfiguration": {
        "logDriver": "awslogs",
        "options": {
          "awslogs-group": "/ecs/trading-sandbox-backend",
          "awslogs-region": "us-east-1",
          "awslogs-stream-prefix": "ecs"
        }
      }
    }
  ]
}
```

Register task definition:
```bash
aws ecs register-task-definition --cli-input-json file://backend-task-definition.json
```

### Step 5: Create ECS Services

```bash
# Create backend service
aws ecs create-service \
  --cluster trading-sandbox-cluster \
  --service-name trading-sandbox-backend \
  --task-definition trading-sandbox-backend:1 \
  --desired-count 1 \
  --launch-type FARGATE \
  --network-configuration "awsvpcConfiguration={subnets=[subnet-xxxxxxxxx],securityGroups=[sg-xxxxxxxxx],assignPublicIp=ENABLED}" \
  --load-balancers "targetGroupArn=arn:aws:elasticloadbalancing:us-east-1:your-account-id:targetgroup/trading-backend/xxxxxxxxx,containerName=backend,containerPort=8080"
```

## Option 3: RDS Database Setup

### Step 1: Create RDS Instance

```bash
# Create subnet group
aws rds create-db-subnet-group \
  --db-subnet-group-name trading-sandbox-subnet-group \
  --db-subnet-group-description "Subnet group for trading sandbox" \
  --subnet-ids subnet-xxxxxxxxx subnet-yyyyyyyyy

# Create RDS instance
aws rds create-db-instance \
  --db-instance-identifier trading-sandbox-db \
  --db-instance-class db.t3.micro \
  --engine postgres \
  --master-username trading_user \
  --master-user-password secure_password_123 \
  --allocated-storage 20 \
  --db-name trading_sandbox \
  --vpc-security-group-ids sg-xxxxxxxxx \
  --db-subnet-group-name trading-sandbox-subnet-group \
  --backup-retention-period 7 \
  --storage-encrypted
```

### Step 2: Configure Security Groups

```bash
# Allow PostgreSQL access
aws ec2 authorize-security-group-ingress \
  --group-id sg-xxxxxxxxx \
  --protocol tcp \
  --port 5432 \
  --source-group sg-xxxxxxxxx
```

## Option 4: Application Load Balancer

### Step 1: Create ALB

```bash
# Create target groups
aws elbv2 create-target-group \
  --name trading-backend-tg \
  --protocol HTTP \
  --port 8080 \
  --vpc-id vpc-xxxxxxxxx \
  --target-type ip \
  --health-check-path /api/actuator/health

aws elbv2 create-target-group \
  --name trading-frontend-tg \
  --protocol HTTP \
  --port 80 \
  --vpc-id vpc-xxxxxxxxx \
  --target-type ip \
  --health-check-path /

# Create load balancer
aws elbv2 create-load-balancer \
  --name trading-sandbox-alb \
  --subnets subnet-xxxxxxxxx subnet-yyyyyyyyy \
  --security-groups sg-xxxxxxxxx

# Create listeners
aws elbv2 create-listener \
  --load-balancer-arn arn:aws:elasticloadbalancing:us-east-1:your-account-id:loadbalancer/app/trading-sandbox-alb/xxxxxxxxx \
  --protocol HTTP \
  --port 80 \
  --default-actions Type=forward,TargetGroupArn=arn:aws:elasticloadbalancing:us-east-1:your-account-id:targetgroup/trading-frontend-tg/xxxxxxxxx
```

## Monitoring and Logging

### CloudWatch Setup

```bash
# Create log groups
aws logs create-log-group --log-group-name /ecs/trading-sandbox-backend
aws logs create-log-group --log-group-name /ecs/trading-sandbox-frontend

# Create dashboard
aws cloudwatch put-dashboard \
  --dashboard-name TradingSandboxDashboard \
  --dashboard-body file://dashboard.json
```

### Dashboard Configuration

Create `dashboard.json`:
```json
{
  "widgets": [
    {
      "type": "metric",
      "properties": {
        "metrics": [
          ["AWS/ECS", "CPUUtilization", "ServiceName", "trading-sandbox-backend"],
          ["AWS/ECS", "MemoryUtilization", "ServiceName", "trading-sandbox-backend"]
        ],
        "period": 300,
        "stat": "Average",
        "region": "us-east-1",
        "title": "ECS Service Metrics"
      }
    }
  ]
}
```

## SSL/TLS Configuration

### Using AWS Certificate Manager

```bash
# Request certificate
aws acm request-certificate \
  --domain-name your-domain.com \
  --validation-method DNS

# Add HTTPS listener to ALB
aws elbv2 create-listener \
  --load-balancer-arn arn:aws:elasticloadbalancing:us-east-1:your-account-id:loadbalancer/app/trading-sandbox-alb/xxxxxxxxx \
  --protocol HTTPS \
  --port 443 \
  --certificates CertificateArn=arn:aws:acm:us-east-1:your-account-id:certificate/xxxxxxxxx \
  --default-actions Type=forward,TargetGroupArn=arn:aws:elasticloadbalancing:us-east-1:your-account-id:targetgroup/trading-frontend-tg/xxxxxxxxx
```

## Backup and Recovery

### Automated Backups

```bash
# Enable automated backups for RDS
aws rds modify-db-instance \
  --db-instance-identifier trading-sandbox-db \
  --backup-retention-period 7 \
  --preferred-backup-window "03:00-04:00" \
  --preferred-maintenance-window "sun:04:00-sun:05:00"
```

### Disaster Recovery

1. **Cross-region replication:**
   ```bash
   # Create read replica in different region
   aws rds create-db-instance-read-replica \
     --db-instance-identifier trading-sandbox-db-dr \
     --source-db-instance-identifier trading-sandbox-db \
     --db-instance-class db.t3.micro \
     --availability-zone us-west-2a
   ```

2. **S3 backup:**
   ```bash
   # Create S3 bucket for backups
   aws s3 mb s3://trading-sandbox-backups
   
   # Enable versioning
   aws s3api put-bucket-versioning \
     --bucket trading-sandbox-backups \
     --versioning-configuration Status=Enabled
   ```

## Cost Optimization

### Reserved Instances

```bash
# Purchase reserved instances for predictable workloads
aws ec2 describe-reserved-instances-offerings \
  --instance-type t3.medium \
  --product-description "Linux/UNIX" \
  --offering-type "All Upfront"
```

### Auto Scaling

```bash
# Create auto scaling group for ECS
aws application-autoscaling register-scalable-target \
  --service-namespace ecs \
  --scalable-dimension ecs:service:DesiredCount \
  --resource-id service/trading-sandbox-cluster/trading-sandbox-backend \
  --min-capacity 1 \
  --max-capacity 5

# Create scaling policy
aws application-autoscaling put-scaling-policy \
  --service-namespace ecs \
  --scalable-dimension ecs:service:DesiredCount \
  --resource-id service/trading-sandbox-cluster/trading-sandbox-backend \
  --policy-name cpu-scaling-policy \
  --policy-type TargetTrackingScaling \
  --target-tracking-scaling-policy-configuration file://scaling-policy.json
```

## Security Best Practices

1. **Use IAM roles instead of access keys**
2. **Enable VPC Flow Logs**
3. **Use Security Groups with minimal required access**
4. **Enable CloudTrail for audit logging**
5. **Use AWS Secrets Manager for sensitive data**
6. **Enable encryption at rest and in transit**

## Troubleshooting

### Common Issues

1. **ECS tasks not starting:**
   ```bash
   # Check task definition
   aws ecs describe-task-definition --task-definition trading-sandbox-backend
   
   # Check service events
   aws ecs describe-services --cluster trading-sandbox-cluster --services trading-sandbox-backend
   ```

2. **Database connection issues:**
   ```bash
   # Check RDS status
   aws rds describe-db-instances --db-instance-identifier trading-sandbox-db
   
   # Check security groups
   aws ec2 describe-security-groups --group-ids sg-xxxxxxxxx
   ```

3. **Load balancer health check failures:**
   ```bash
   # Check target group health
   aws elbv2 describe-target-health --target-group-arn arn:aws:elasticloadbalancing:us-east-1:your-account-id:targetgroup/trading-backend-tg/xxxxxxxxx
   ```

## Maintenance

### Regular Tasks

1. **Update Docker images monthly**
2. **Rotate database passwords quarterly**
3. **Review CloudWatch logs weekly**
4. **Update security patches as needed**
5. **Monitor costs and optimize resources**

### Scaling Considerations

- **Horizontal scaling:** Add more ECS tasks
- **Vertical scaling:** Increase CPU/memory allocation
- **Database scaling:** Use RDS read replicas
- **Caching:** Add ElastiCache for Redis 