package kor.it.academy.common.vo;

public enum CountryEnum {
    KOREA("kr", "대한민국"),
    KOREA2("ko", "대한민국"),
    AMERICA("us", "미국"),
    EUROPE("eu", "유럽"),
    UK("GB", "영국");

    private final String code;
    private final String countryName;

    CountryEnum(String code, String countryName){
        this.code = code;
        this.countryName = countryName;
    }

    public static String getCountryName(String code){
        for(CountryEnum countryEnum : CountryEnum.values()){
            if(countryEnum.code.equalsIgnoreCase(code)){
                return countryEnum.countryName;
            }
        }
        return null;
    }
}
