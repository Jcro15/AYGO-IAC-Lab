package co.edu.escuelaing;

import co.edu.escuelaing.props.Ec2Props;
import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

public class CdkInfraApp {
    public static void main(final String[] args) {
        App app = new App();

        Ec2Props props = new Ec2Props();
        props.setIamRoleName((String)app.getNode().tryGetContext("roleName"));
        props.setVpcId((String)app.getNode().tryGetContext("vpcId"));
        props.setAppImage((String)app.getNode().tryGetContext("appImage"));

        new EC2WebAppStack(app, "CdkInfraStack", StackProps.builder()
                .env(Environment.builder()
                        .account(System.getenv("CDK_DEFAULT_ACCOUNT"))
                        .region("us-east-1")
                    .build())
                .build(),
                props);

        app.synth();
    }
}

