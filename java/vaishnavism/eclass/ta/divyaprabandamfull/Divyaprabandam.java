package vaishnavism.eclass.ta.divyaprabandamfull;

/**
 * Created by spillaip on 2/25/2018.
 */

public class Divyaprabandam {
    private String PaasuramText;
    private String PaasuramMetaData;
    private int Azhwaar;

    public Divyaprabandam(String paasuramText, String paasuramMetaData, int azhwaar) {
        PaasuramText = paasuramText;
        PaasuramMetaData = paasuramMetaData;
        Azhwaar = azhwaar;
    }

    public int getAzhwaar() {
        return Azhwaar;
    }

    public void setAzhwaar(int azhwaar) {
        Azhwaar = azhwaar;
    }



    public String getPaasuramText() {
        return PaasuramText;
    }

    public void setPaasuramText(String paasuramText) {
        PaasuramText = paasuramText;
    }

    public String getPaasuramMetaData() {
        return PaasuramMetaData;
    }

    public void setPaasuramMetaData(String paasuramMetaData) {
        PaasuramMetaData = paasuramMetaData;
    }
}
