package utils;

public class FrameRelogio {
    public int pagina;
    public boolean bitR;

    public FrameRelogio(int pagina) {
        this.pagina = pagina;
        this.bitR = true;
    }

    @Override
    public String toString() {
        return pagina + "(R=" + (bitR ? "1" : "0") + ")";
    }
}
