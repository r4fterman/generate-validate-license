#!/usr/bin/env bash

DIR=`dirname $0`

# build application
mvn clean install -q

# create necessary key files
./create_private_public_keys.sh

# Sign text
echo ""
echo "Signing text..."
java -jar ./target/license-check.jar -pk "${DIR}"/privateKey_pcks8.pem -u me@yahoo.com > signature.txt
SIGNATURE=$(sed 1d signature.txt)
rm -f signature.txt
echo "Signature: ${SIGNATURE}"

# Verify signed text
echo ""
echo "Verifying text..."
java -jar ./target/license-check.jar -v ${DIR}/publicKey.pem -u me@yahoo.com -s ${SIGNATURE}

# remove key files
rm -f privateKey.pem
rm -f privateKey_pcks8.pem
rm -f publicKey.pem
