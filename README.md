[![Build Status](https://travis-ci.org/r4fterman/generate-validate-license.svg?branch=master)](https://travis-ci.org/r4fterman/generate-validate-license)
[![Known Vulnerabilities](https://snyk.io/test/github/r4fterman/generate-validate-license/badge.svg?targetFile=pom.xml)](https://snyk.io/test/github/r4fterman/generate-validate-license?targetFile=pom.xml)
[![Maintainability](https://api.codeclimate.com/v1/badges/10e5d8a936e65a3e38d3/maintainability)](https://codeclimate.com/github/r4fterman/generate-validate-license/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/10e5d8a936e65a3e38d3/test_coverage)](https://codeclimate.com/github/r4fterman/generate-validate-license/test_coverage)

# Introduction
Inspired by this [article](https://build-system.fman.io/generating-license-keys) this is an implementation of a proof for an offline license check in Java.

# Requirements
- Java 11 or above
- openssl

# Execute

    $>./proof.sh
    
# What it does
Executing the shell script does 3 things:
- Generate private/public key pair
- Create a signature for a text using the private key
- Verify the signature on a text using the public key