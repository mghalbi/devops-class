terraform {
  required_providers {
    aws = {
      source = "hashicorp/aws"
      version = "4.32.0"
    }
  }
}

provider "aws" {
  region = "eu-south-1"
}


# resource "aws_instance" "my-first-instance" {
#   ami = "ami-0579ab55007adb044"
#   instance_type = "t3.micro"
#   tags = {
#     "Name" = "my-first-instance"
#   }
# }


resource "aws_vpc" "firs-vpc" {
  cidr_block = "10.0.0.0/16"  
  tags = {
    Name = "prod"
  }
}


resource "aws_subnet" "first-subnet" {
  cidr_block = "10.0.1.0/24"
  vpc_id = aws_vpc.firs-vpc.id
  tags = {
    Name = "first-subnet"
  } 
}