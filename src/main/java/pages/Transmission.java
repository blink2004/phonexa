package pages;

public enum Transmission {
    ALL("f-all-filter_enum_transmission_type_47"),
    MECHANICAL("f-545_transmission_type"),
    AUTOMATIC("f-546_transmission_type"),
    VARIABLE("f-547_transmission_type"),
    ADAPTIVE("f-adaptive_transmission_type"),
    TIPTRONIC("f-tip-tronic_transmission_type");

    private String checkboxId;

    Transmission(String locator) {
        this.checkboxId = locator;
    }

    public static String getLocatorByName(Transmission value) {
        for (int i = 0; i < Transmission.values().length; i++) {
            if (value.toString().equals(Transmission.values()[i].name()))
                return Transmission.values()[i].checkboxId;
        }
        return null;
    }
}
