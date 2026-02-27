package steps;

import config.DriverFactory;
import config.TestConfig;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.LoggPage;
import pages.RegisterPage;

import java.time.Duration;
import java.util.Set;

public class AgencyRegisterPageSteps {

    private final WebDriver driver = DriverFactory.getDriver();
    private final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestConfig.WAIT_SEC));

    private LoggPage agreementPage;
    private RegisterPage registerPage;
    private RegisterPage.AgencyFormData data;
    private final By yeniAcenteUyelikLink = By.cssSelector("a.new-agency-btn");

    @Given("Kullanıcı acente Ana Sayfasina gidilir")
    public void userGoesToHome() {
        driver.get(TestConfig.BASE_URL);
    }

    @When("Yeni Acente Uyelik linkine tiklanilip yeni sekmeye gecilir")
    public void clickNewAgencyAndSwitchTab() {
        String mainHandle = driver.getWindowHandle();
        wait.until(ExpectedConditions.elementToBeClickable(yeniAcenteUyelikLink)).click();

        wait.until(d -> d.getWindowHandles().size() > 1);
        Set<String> handles = driver.getWindowHandles();
        for (String h : handles) {
            if (!h.equals(mainHandle)) {
                driver.switchTo().window(h);
                break;
            }
        }

        agreementPage = new LoggPage(driver);
        registerPage = new RegisterPage(driver);
        data = new RegisterPage.AgencyFormData();
    }

    @And("Sozlesmeyi kabul eder ve devam eder")
    public void acceptAgreementAndContinue() {
        agreementPage.acceptContractAndContinue();
    }

    @Then("Bos formda zorunlu alan validasyonlari gorunur")
    public void shouldSeeValidationErrorsOnEmptyForm() {
        registerPage.clickKaydet();
        registerPage.assertHasAnyValidationError();
    }

    @When("Tum zorunlu alanlari kurallara uygun doldurur")
    public void fillAllRequiredFields() {
        registerPage.fillAllFieldsHappyPath(null);
    }

    @And("Kaydet butonuna basar")
    public void clickSave() {
        registerPage.clickKaydetAndWaitResult();
    }

    @Then("Islem basarili mesaji gorunur")
    public void successMessageVisible() {
        registerPage.assertSuccessAfterSave();
    }
//--------------------------------------------------------------------------------------------------------------------------------------------
@When("Email gecersiz formatta override edilir")
public void overrideInvalidEmail() {
    data.agencyEmail = "invalidmail.com";  // override
    registerPage.fillAllFieldsHappyPath(data);
}

    @When("Email bos override edilir")
    public void overrideEmptyEmail() {
        data.agencyEmail = "";  // override
        registerPage.fillAllFieldsHappyPath(data);
    }

    @When("Telefon alanina harf karakter girilir")
    public void overrideInvalidPhone() {
        data.mobilePhone = "abc123xyz";  // override
        registerPage.fillAllFieldsHappyPath(data);
    }

    @When("Sifre özel karakter içermeden veri girilir")
    public void overrideNoneCaracterPassword() {
        data.password = "12Mk345";  // override
        registerPage.fillAllFieldsHappyPath(data);
    }

    @When("Sifre 5 karakter olarak girilir")
    public void overrideShortPassword() {
        data.password = "12Mk!";  // override
        registerPage.fillAllFieldsHappyPath(data);
    }
    @When("Sifre tekrar farkli override edilir")
    public void overridePasswordMismatch() {

        data.password = "Test1234!";
        registerPage.fillAllFieldsHappyPath(data);

        // Tekrar alanı ayrıca eziliyor
        driver.findElement(By.id("passwordRepeat")).clear();
        driver.findElement(By.id("passwordRepeat")).sendKeys("Test9999!");
    }


    @When("Acente adi minimum karakter sinirinda girilir")
    public void overrideMinAgencyName() {
        data.agencyName = "ABC";
        registerPage.fillAllFieldsHappyPath(data);
    }

    @When("Kullanilmis acente adi girilir")
    public void overrideMaxAgencyName() {
        data.agencyName = "TESTHASANKILIC0605";
        registerPage.fillAllFieldsHappyPath(data);
    }


    @When("IBAN eksik karakterle girilir")
    public void eksikIBAN() {
        data.iban = "TR324234";
        registerPage.fillAllFieldsHappyPath(data);
    }
    @When("Kayitli email override edilir")
    public void overrideDuplicateEmail() {
        data.agencyEmail = "test_hasankilic@hotmail.com";
        registerPage.fillAllFieldsHappyPath(data);
    }

    @When("Email alani bos birakilir")
    public void noneEmail() {
        data.agencyEmail = "";
        registerPage.fillAllFieldsHappyPath(data);
    }



    @When("Adres Detayı alanına ozel karakterler girilir")
    public void specialAdress() {
        data.addressDetail = "'^+^'+'^+'^+'^+'^+'^+'^+";
        registerPage.fillAllFieldsHappyPath(data);
    }


    @When("Yol tarifi  ozel karakterler girilir")
    public void specialStreet() {
        data.directions = "'^+^'+'^+'^+'^+'^+'^+'^+";
        registerPage.fillAllFieldsHappyPath(data);
    }
    //---------------------------------------------------------------------------------------------------------------------------------------------------------------------

    // ==========================
// ORTAK FORM AÇMA
// ==========================

    @Given("Kayit formu aciktir")
    public void registrationFormIsOpen() {
        userGoesToHome();
        clickNewAgencyAndSwitchTab();
        acceptAgreementAndContinue();
    }


// ==========================
// NEGATIVE SENARYOLAR
// ==========================



    @Given("Daha once kayitli bir email ile form doldurulur")
    public void duplicateEmailCase() {
        registrationFormIsOpen();
        RegisterPage.AgencyFormData data = new RegisterPage.AgencyFormData();
        registerPage.fillAllFieldsHappyPath(data);
        registerPage.fillDuplicateEmail("test_hasankilic@hotmail.com");
    }


// ==========================
// GENERIC VALIDATION ASSERT
// ==========================

    @Then("Email icin validation hatasi gorulmelidir")
    @Then("Telefon icin validation hatasi gorulmelidir")
    @Then("Sifre minimum karakter hatasi gorulmelidir")
    @Then("Acente adi kullanilmis hatasi gorulmelidir")
    @Then("Email zorunlu alan hatasi gorulmelidir")
    @Then("IBAN eksik karakterle girildi hata mesaji gorulmeli")
    @Then("Sifre uyusmazlik hatasi gorulmelmelidir")
    @Then("Email zaten kayitli hatasi gorulmelidir")
    @Then("Zorunlu alan hata mesajlari goruntulenmelidir")
    @Then("Sifre karakter validasyon hatasi gorulmelmelidir")
    @Then("Islem basarisiz mesaji gorunur")
    @Then("Validasyon hatasi gorulur")
    @Then("Adres detay alani validasyon hatasi görülür")
    public void validationErrorVisible() {
        registerPage.assertErrorAfterSave();
        //registerPage.assertSuccessAfterSave();
    }
}