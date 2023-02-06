package vishwakarma.matrimony.seva.model;

public class FeesModel {
    String id;
    String sid;
    String amt;
    String pmode;
    String details;
    String cdate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }

    public String getPmode() {
        return pmode;
    }

    public void setPmode(String pmode) {
        this.pmode = pmode;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public String getInstallmentno() {
        return installmentno;
    }

    public void setInstallmentno(String installmentno) {
        this.installmentno = installmentno;
    }

    String installmentno;
}
