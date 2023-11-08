#!/bin/bash
# Read configuration values from the file
config_file="configs.txt"
source "$config_file"

# Build app and create image
if [ "$build_app" = true ]; then
  mvn -f ./SparkServer/pom.xml clean install
  wait
  docker build --tag dockersparkprimer .
  wait
  docker tag dockersparkprimer "$appImage"
  docker push "$appImage"
  wait
fi

cd cdk

# Run the 'cdk synth' with context values
cdk synth --context "vpcId=$vpcId" \
          --context "roleName=$roleName" \
          --context "port=$appPort" \
          --context "appImage=$appImage"
wait

TEMPLATE_FILE="cdk.out/CdkInfraStack.template.json"
# Remove cdk bootstrap dependencies / failing sections
jq 'del(.Parameters.BootstrapVersion)' "$TEMPLATE_FILE" > "$TEMPLATE_FILE.tmp" && mv "$TEMPLATE_FILE.tmp" "$TEMPLATE_FILE"
jq 'del(.Rules)' "$TEMPLATE_FILE" > "$TEMPLATE_FILE.tmp" && mv "$TEMPLATE_FILE.tmp" "$TEMPLATE_FILE"
# Execute cloudformation
aws cloudformation create-stack --stack-name SimpleEc2WebAppStack --template-body file://cdk.out/CdkInfraStack.template.json --capabilities CAPABILITY_IAM