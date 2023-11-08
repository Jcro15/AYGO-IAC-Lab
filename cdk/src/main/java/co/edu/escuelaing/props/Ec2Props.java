package co.edu.escuelaing.props;

public class Ec2Props {
    private String iamRoleName;
    private String vpcId;
    private String appImage;

    public String getIamRoleName() {
        return iamRoleName;
    }

    public void setIamRoleName(String iamRoleName) {
        this.iamRoleName = iamRoleName;
    }

    public String getVpcId() {
        return vpcId;
    }

    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    public String getAppImage() {
        return appImage;
    }

    public void setAppImage(String appImage) {
        this.appImage = appImage;
    }
}
