#!/usr/bin/env bash

PRIVATE_KEY_FOLDER=./generate-license/src/main/resources
PRIVATE_KEY_FILE=${PRIVATE_KEY_FOLDER}/privateKey.pem
PCKS8_FILE=${PRIVATE_KEY_FOLDER}/privateKey_pcks8.pem

PUBLIC_KEY_FOLDER=.
PUBLIC_KEY_FILE=${PUBLIC_KEY_FOLDER}/public.pem

# create private key
openssl genrsa -out ${PRIVATE_KEY_FILE} 4096

# create corresponding public key
openssl rsa -in ${PRIVATE_KEY_FILE} -outform PEM -pubout -out ${PUBLIC_KEY_FILE}

# create private key in PCKS8 format
openssl pkcs8 -topk8 -inform PEM -in ${PRIVATE_KEY_FILE} -out ${PCKS8_FILE} -nocrypt

echo ""
echo "Public/Private key generation successful."
echo "Private key written to ${PRIVATE_KEY_FILE}"
echo "PCKS8 file written to ${PCKS8_FILE}"
echo "Public key written to ${PUBLIC_KEY_FILE}"
