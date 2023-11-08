package co.edu.escuelaing;

import co.edu.escuelaing.props.Ec2Props;
import org.jetbrains.annotations.NotNull;
import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.iam.IRole;
import software.amazon.awscdk.services.iam.Role;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import java.util.LinkedList;

public class EC2WebAppStack extends Stack {
    public EC2WebAppStack(final Construct scope, final String id, final StackProps props, Ec2Props ec2Props) {
        super(scope, id, props);
        int NumberOfInstances = 3;


        final IVpc vpc = Vpc.fromLookup(this,"main-vpc", VpcLookupOptions.builder().vpcId(ec2Props.getVpcId()).build());
        final SecurityGroup sg = createSecurityGroup(vpc);

        final UserData userData = UserData.forLinux();
        userData.addCommands(
                "sudo yum update -y",
                "sudo yum install -y docker",
                "sudo service docker start",
                "sudo docker run -d -p 80:6000 "+ec2Props.getAppImage()

        );
        IRole labRole = Role.fromRoleName(this, "LabRole", ec2Props.getIamRoleName());

        for (int i = 0; i < NumberOfInstances; i++) {
            createInstance("ec2-instace-"+i, vpc, sg, labRole, userData);
        }
    }

    private SecurityGroup createSecurityGroup(IVpc vpc) {
        final SecurityGroup sg = SecurityGroup.Builder.create(this, "WebSg")
                .vpc(vpc)
                .securityGroupName("webAccessSg")
                .build();
        sg.addIngressRule(Peer.anyIpv4(), Port.tcp(80),"Http Ingress port");
        return sg;
    }

    private Instance createInstance(String id, IVpc vpc, SecurityGroup sg, IRole labRole, UserData userData) {
        return Instance.Builder
                .create(this, id)
                .vpc(vpc)
                .securityGroup(sg)
                .vpcSubnets(
                        SubnetSelection.builder()
                                .subnetType(SubnetType.PUBLIC)
                                .build()
                )
                .role(labRole)
                .instanceType(InstanceType.of(InstanceClass.T3, InstanceSize.MICRO))
                .machineImage(MachineImage.latestAmazonLinux2())
                .userData(userData)
                .build();
    }
}
