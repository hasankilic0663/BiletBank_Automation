package pages;

import config.TestConfig;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class RegisterPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By kaydetBtn = By.xpath("//button[normalize-space()='Kaydet' or normalize-space()='KAYDET' or contains(@class,'bb-btn')]");
    private final By loaderBox = By.cssSelector("#loader-box");

    // ======= WAIT LOCATORS (RegisterPage içine ekle) =======
    private final By successToast = By.xpath("//*[contains(.,'İşlem başarılı') or contains(.,'Başarılı') or contains(.,'Kayıt başarılı')][not(self::script)]");
    private final By successBox = By.xpath("//*[contains(@class,'ivu-alert') and contains(@class,'success')][.//*[contains(normalize-space(.),'İşlem Başarılı')]]");
    private final By errorBox = By.xpath("//*[contains(@class,'ivu-alert') and (contains(@class,'error') or contains(@class,'warning'))]");


    private void waitLoaderToDisappear() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(30))
                    .until(ExpectedConditions.invisibilityOfElementLocated(loaderBox));
        } catch (Exception ignore) {
        }
    }

    public void clickKaydetAndWaitResult() {
        waitLoaderToDisappear();

        WebElement btn = new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(ExpectedConditions.visibilityOfElementLocated(kaydetBtn));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", btn);

        try {
            btn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }

        waitLoaderToDisappear();

        new WebDriverWait(driver, Duration.ofSeconds(30)).until(d ->
                !d.findElements(successBox).isEmpty() || !d.findElements(errorBox).isEmpty()
        );
    }

    public void assertSuccessAfterSave() {
        clickKaydetAndWaitResult();

        if (driver.findElements(successToast).stream().noneMatch(WebElement::isDisplayed)) {
            String pageText = driver.findElement(By.tagName("body")).getText();
            throw new AssertionError("Kayıt başarısız. Sayfa mesajları:\n" + pageText);
        }
    }
    public void assertErrorAfterSave() {
        clickKaydetAndWaitResult();

        if (driver.findElements(errorBox).stream().noneMatch(WebElement::isDisplayed)) {
            String pageText = driver.findElement(By.tagName("body")).getText();
            throw new AssertionError("Kayıt başarısız. Sayfa mesajları:\n" + pageText);
        }
    }
    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(TestConfig.WAIT_SEC));
    }

    private static long COUNTER = 0;

    public static String token(String prefix, int len) {
        long c = ++COUNTER;
        long t = System.nanoTime();
        String raw = prefix + "_" + t + "_" + c + "_" + Integer.toHexString((prefix + t + c).hashCode());
        String cleaned = raw.replaceAll("[^A-Za-z0-9]", "");
        if (cleaned.length() < len) cleaned = (cleaned + "X".repeat(len)).substring(0, len);
        return cleaned.substring(0, len);
    }


public  static String uniqueAgencyNameNoSpace() {
    return "TEST-HasanKilic" + token("A", 12);
}
    public static String uniqueUsernameNoSpace() {
        return ("TEST_HASAN" + token("U", 18)).substring(0, 20);
    }

    private void setMaskedPhoneByPrefixName(String prefixDivName, String phoneAnyFormat) {
        String onlyDigits = phoneAnyFormat.replaceAll("\\D", "");

        By phoneInput = By.xpath(
                "//div[@name='" + prefixDivName + "']" +
                        "//div[contains(@class,'col-8') or contains(@class,'col-md-9')]" +
                        "//input[@type='text' and contains(@class,'bltbnk-textbox')]"
        );

        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(phoneInput));
        wait.until(ExpectedConditions.elementToBeClickable(el));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);

        try {
            new Actions(driver).moveToElement(el).pause(Duration.ofMillis(150)).click().perform();
        } catch (Exception ignore) {
            el.click();
        }

        try {
            el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            el.sendKeys(Keys.DELETE);
        } catch (Exception ignore) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].value='';", el);
        }

        for (char ch : onlyDigits.toCharArray()) {
            el.sendKeys(String.valueOf(ch));
            try {
                Thread.sleep(20);
            } catch (InterruptedException ignored) {
            }
        }
        try {
            el.sendKeys(Keys.TAB);
        } catch (Exception ignore) {
        }
    }

    private WebElement inputBySectionAndLabel(String sectionTitle, String labelText) {
        String xpath =
                "//*[contains(@class,'bb-box')]" +
                        "[.//div[contains(@class,'box-title') and normalize-space()='" + sectionTitle + "']]" +
                        "//*[self::div and contains(@class,'fs12') and normalize-space()='" + labelText + "']" +
                        "/following::input[contains(@class,'bltbnk-textbox')][1]";
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
    }

    private WebElement selectBySectionAndLabel(String sectionTitle, String labelText) {
        String xpath =
                "//*[contains(@class,'bb-box')]" +
                        "[.//div[contains(@class,'box-title') and normalize-space()='" + sectionTitle + "']]" +
                        "//*[self::div and contains(@class,'fs12') and normalize-space()='" + labelText + "']" +
                        "/following::select[1]";
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
    }

    private WebElement textareaBySectionAndLabel(String sectionTitle, String labelText) {
        String xpath =
                "//*[contains(@class,'bb-box')]" +
                        "[.//div[contains(@class,'box-title') and normalize-space()='" + sectionTitle + "']]" +
                        "//*[self::div and contains(@class,'fs12') and normalize-space()='" + labelText + "']" +
                        "/following::textarea[1]";
        return wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
    }

    private void clearType(WebElement el, String value) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);

        try {
            driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
        } catch (Exception ignore) {
        }
        try {
            driver.findElement(By.tagName("body")).click();
        } catch (Exception ignore) {
        }

        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector(".ivu-select-dropdown")));
        } catch (Exception ignore) {
        }

        try {
            wait.until(ExpectedConditions.elementToBeClickable(el)).click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        }

        try {
            el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
            el.sendKeys(Keys.DELETE);
            el.sendKeys(value);
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].value='';" +
                            "arguments[0].dispatchEvent(new Event('input',{bubbles:true}));" +
                            "arguments[0].dispatchEvent(new Event('change',{bubbles:true}));",
                    el
            );
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].value=arguments[1];" +
                            "arguments[0].dispatchEvent(new Event('input',{bubbles:true}));" +
                            "arguments[0].dispatchEvent(new Event('change',{bubbles:true}));",
                    el, value
            );
        }

        try {
            el.sendKeys(Keys.TAB);
        } catch (Exception ignore) {
        }
    }

    private WebElement optional(By by) {
        List<WebElement> els = driver.findElements(by);
        return (els == null || els.isEmpty()) ? null : els.get(0);
    }


    private void selectFirstNonDefault(WebElement selectEl) {
        Select s = new Select(selectEl);
        List<WebElement> opts = s.getOptions();
        for (int i = 0; i < opts.size(); i++) {
            String txt = opts.get(i).getText() == null ? "" : opts.get(i).getText().trim();
            if (!txt.isEmpty() && !txt.equalsIgnoreCase("Seçiniz")) {
                s.selectByIndex(i);
                return;
            }
        }

    }


    private void waitDistrictOptionsLoaded(WebElement districtSelect) {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(10));
        w.until(d -> {
            try {
                Select s = new Select(districtSelect);
                List<WebElement> opts = s.getOptions();

                return opts != null && opts.size() >= 2;
            } catch (StaleElementReferenceException ignored) {
                return false;
            }
        });
    }


    private By anyValidationError() {
        return By.xpath(
                "//*[contains(@class,'ivu-form-item-error-tip') or contains(@class,'ivu-form-item-error')]" +
                        "|//*[contains(@class,'ivu-alert') and (contains(@class,'error') or contains(@class,'warning'))]//span" +

                        "|//*[self::div or self::span or self::p][normalize-space()='Lütfen, Alanları Boş Bırakmayın']" +
                        "|//*[self::div or self::span or self::p][contains(.,'Lütfen en az 8 karakter')]"
        );
    }

    public void clickKaydet() {
        WebElement btn = wait.until(ExpectedConditions.presenceOfElementLocated(kaydetBtn));
        wait.until(ExpectedConditions.elementToBeClickable(btn));
        try {
            btn.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
        }
    }//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

    public static class AgencyFormData {

        public String agencyName = uniqueAgencyNameNoSpace();
        public String agencyEmail = "testhasan01@mail.com";
        public String phone = "3125551122";
        public String mobilePhone = "5325551122";
        public String fax = "3125553344";
        public String tursabNo = "123456789012345678901234567890";

        public String sehir = "ANKARA";
        public String semt = "KIZILAY";
        public String street = "ATATURK BULVARI";
        public String addressDetail = "NO: 10 KAT: 3";
        public String directions = "KIZILAY MEYDAN YANI";

        public String invoiceTitle = "TEST HASAN KILIC LTD";
        public String taxOffice = "CANKAYA";
        public String taxNumber = "12345678901";
        public String invoiceAddress = "ANKARA MERKEZ ADRES";
        public String bankName = "ZIRAAT BANKASI";
        public String invoiceCity = "ANKARA";
        public String iban = "TR120006200000000012345678";

        public String managerName = "HASAN KILIC";
        public String managerPhone = "5324445566";
        public String managerEmail = "yonetici@testmail.com";

        public String username = uniqueUsernameNoSpace();
        public String userEmail = "kullanici@testmail.com";
        public String password = "Test1234!";
    }


    public void fillAllFieldsHappyPath(AgencyFormData data) {


        if (data == null) {
            data = new AgencyFormData(); // default
        }

        fillAgencyBasicInformation(data);
        fillAgencyLocationInformation(data);
        fillAgencyActivityInformation();
        fillInvoiceInformation(data);
        fillManagerInformation(data);
        fillUserAccountInformation(data);
    }

// =======================================================
// 1 ACENTE TEMEL BİLGİLERİ
// =======================================================

    private void fillAgencyBasicInformation(AgencyFormData data) {
        enterAgencyName(data);
        enterAgencyEmail(data);
        enterAgencyPhones(data);
        enterTursabDocumentNumber(data);
        selectAgencyClassAndCountry();
    }

    private void enterAgencyName(AgencyFormData data) {
        //String agencyName = "TEST HASAN KILIC 01";
        clearType(inputBySectionAndLabel("Acente Bilgileri", "Acente Adı"), data.agencyName);
        System.setProperty("LAST_AGENCY_NAME", data.agencyName);
    }

    private void enterAgencyEmail(AgencyFormData data) {
        clearType(inputBySectionAndLabel("Acente Bilgileri", "Mail Adresi"), data.agencyEmail);
    }

    private void enterAgencyPhones(AgencyFormData data) {
        setMaskedPhoneByPrefixName("txtPhonePrefix", data.phone);
        setMaskedPhoneByPrefixName("txtMobilePhonePrefix", data.mobilePhone);
        setMaskedPhoneByPrefixName("txtFaxPhonePrefix", data.fax);
    }

    private void enterTursabDocumentNumber(AgencyFormData data) {
        WebElement tursab = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//*[contains(@class,'bb-box')][.//div[contains(@class,'box-title') and normalize-space()='Acente Bilgileri']]//*[normalize-space()='Türsab Belge No']/following::input[contains(@class,'bltbnk-textbox')][1]")
        ));
        clearType(tursab, data.tursabNo);
    }

    private void selectAgencyClassAndCountry() {
        new Select(selectBySectionAndLabel("Acente Bilgileri", "Acente Sınıfı")).selectByValue("A");
        new Select(selectBySectionAndLabel("Acente Bilgileri", "Ülke")).selectByValue("TR");
    }

// =======================================================
// 2 LOKASYON BİLGİLERİ
// =======================================================

    private void fillAgencyLocationInformation(AgencyFormData data) {
        selectCityAndDistrict(data);
        enterAddressDetails(data);
        enterDirections(data);
    }

    private void selectCityAndDistrict(AgencyFormData data) {

        WebElement citySelect = selectBySectionAndLabel("Acente Bilgileri", "Şehir");
        Select city = new Select(citySelect);
        city.selectByVisibleText(data.sehir);

        try { citySelect.sendKeys(Keys.TAB); } catch (Exception ignore) {}

        try {
            WebElement districtSelect = selectBySectionAndLabel("Acente Bilgileri", "İlçe");
            waitDistrictOptionsLoaded(districtSelect);
            selectFirstNonDefault(districtSelect);
            districtSelect.sendKeys(Keys.TAB);
        } catch (TimeoutException | NoSuchElementException e) {
            clearType(inputBySectionAndLabel("Acente Bilgileri", "İlçe"), "ÇANKAYA");
        }
    }

    private void enterAddressDetails(AgencyFormData data) {
        clearType(inputBySectionAndLabel("Acente Bilgileri", "Semt"), data.semt);
        clearType(inputBySectionAndLabel("Acente Bilgileri", "Cadde / Bulvar"), data.street);
        clearType(inputBySectionAndLabel("Acente Bilgileri", "Adres Detayı"), data.addressDetail);
    }

    private void enterDirections(AgencyFormData data) {

        WebElement yolTarifi = textareaBySectionAndLabel("Acente Bilgileri", "Yol Tarifi");

        wait.until(ExpectedConditions.elementToBeClickable(yolTarifi));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", yolTarifi);

        yolTarifi.click();
        yolTarifi.sendKeys(data.directions);

        try { yolTarifi.sendKeys(Keys.TAB); } catch (Exception ignore) {}
    }

// =======================================================
// 3 FAALİYET ALANI
// =======================================================

    private void fillAgencyActivityInformation() {

        By ucakCheckbox = By.xpath(
                "//*[contains(@class,'bb-box')][.//div[contains(@class,'box-title') and normalize-space()='Acente Bilgileri']]" +
                        "//*[contains(.,'Acente Faaliyet Alanları')]/following::*[self::label or self::span or self::div][normalize-space()='Uçak' or normalize-space()='Ucak'][1]"
        );

        WebElement ucak = optional(ucakCheckbox);

        if (ucak != null) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", ucak);
            ucak.click();
        }
    }

// =======================================================
// 4️⃣ FATURA BİLGİLERİ
// =======================================================


    private void fillInvoiceInformation(AgencyFormData data) {


        clearType(inputBySectionAndLabel("Fatura Bilgileri", "Fatura Ünvanı"), data.invoiceTitle);
        clearType(inputBySectionAndLabel("Fatura Bilgileri", "Vergi Dairesi"), data.taxOffice);
        clearType(inputBySectionAndLabel("Fatura Bilgileri", "Vergi Numarası"), data.taxNumber);
        clearType(inputBySectionAndLabel("Fatura Bilgileri", "Fatura Adresi"), data.invoiceAddress);
        clearType(inputBySectionAndLabel("Fatura Bilgileri", "Banka İsmi"), data.bankName);
        clearType(inputBySectionAndLabel("Fatura Bilgileri", "Fatura Şehri"), data.invoiceCity);
        clearType(inputBySectionAndLabel("Fatura Bilgileri", "IBAN"), data.iban);
    }

// =======================================================
// 5️⃣ YÖNETİCİ BİLGİLERİ
// =======================================================

    private void fillManagerInformation(AgencyFormData data) {

        clearType(inputBySectionAndLabel("Yönetici Bilgileri", "Şirket Yetkilisi Adı ve Soyadı"), data.managerName);
        setMaskedPhoneByPrefixName("txtManagerMobilePhonePrefix", data.managerPhone);
        clearType(inputBySectionAndLabel("Yönetici Bilgileri", "Mail Adresi"), data.managerEmail);
    }

// =======================================================
// 6️⃣ KULLANICI HESABI
// =======================================================

    private void fillUserAccountInformation(AgencyFormData data) {

        clearType(inputBySectionAndLabel("Kullanıcı Giriş Bilgileri", "Kullanıcı Adı"), data.username);
        clearType(inputBySectionAndLabel("Kullanıcı Giriş Bilgileri", "Mail Adresi"), data.userEmail);


        WebElement password = optional(By.id("password"));
        if (password == null) {
            By pwByLabel = By.xpath(
                    "//*[contains(@class,'bb-box')][.//div[contains(@class,'box-title') and normalize-space()='Kullanıcı Giriş Bilgileri']]" +
                            "//*[self::div and contains(@class,'fs12') and normalize-space()='Şifre']" +
                            "/following::input[@type='password'][1]"
            );
            password = wait.until(ExpectedConditions.presenceOfElementLocated(pwByLabel));
        }

        clearType(password, data.password);

        WebElement passwordRepeat = optional(By.xpath(
                "//*[contains(@class,'bb-box')][.//div[contains(@class,'box-title') and normalize-space()='Kullanıcı Giriş Bilgileri']]" +
                        "//*[self::div and contains(@class,'fs12') and (normalize-space()='Şifre Tekrar' or normalize-space()='Şifre (Tekrar)' or normalize-space()='Şifre Onay')]" +
                        "/following::input[@type='password'][1]"
        ));

        if (passwordRepeat != null) {
            clearType(passwordRepeat, data.password);
        }
    }


    public void fillDuplicateEmail(String email) {
        clearType(inputBySectionAndLabel("Acente Bilgileri", "Mail Adresi"), email);
    }


// ==========================
// GENERIC VALIDATION ASSERT
// ==========================


    public void assertHasAnyValidationError() {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(10));
        w.until(ExpectedConditions.presenceOfElementLocated(anyValidationError()));

        List<WebElement> errs = driver.findElements(anyValidationError());
        if (errs == null || errs.isEmpty()) {
            throw new AssertionError("Validation/hata mesajı bekleniyordu ama bulunamadı.");
        }
    }
}