# BiletBank_Automation
# 🎟 BiletBank Acente Kayıt Otomasyon Projesi

Bu proje, BiletBank Acente Kayıt ekranının **Verification & Validation** testlerini otomatikleştirmek amacıyla geliştirilmiştir.

Testler aşağıdaki adres üzerinden gerçekleştirilmiştir:

🔗 

---

## 🚀 Kullanılan Teknolojiler

- Java 17
- Maven
- Selenium WebDriver
- Cucumber (BDD)
- TestNG
- Page Object Model (POM)
- WebDriverWait (Explicit Wait)
- Git & GitHub

---

## 📂 Proje Mimarisi

Proje **Page Object Model (POM)** tasarım desenine uygun geliştirilmiştir.

src
└── test
├── java
│   ├── config
│   │    ├── DriverFactory.java
│   │    └── TestConfig.java
│   │
│   ├── pages
│   │    ├── LoggPage.java
│   │    └── RegisterPage.java
│   │
│   ├── steps
│   │    └── AgencyRegisterSteps.java
│   │
│   └── runners
│        └── TestRunner.java
│
└── resources
└── features
└── agencyRegister.feature

---

## ✅ Test Kapsamı

### 🔹 Pozitif Senaryo
- Tüm zorunlu alanlar doğru doldurulduğunda başarılı kayıt

### 🔹 Negatif Senaryolar
- Geçersiz email formatı
- Email boş bırakılması
- Telefon harfli girilmesi
- Eksik telefon numarası
- Minimum karakter altı acente adı
- Hatalı vergi numarası
- Eksik IBAN
- Şifre policy ihlali
- Şifre uyuşmazlığı
- Duplicate email
- Zorunlu alanların boş bırakılması

### 🔹 Boundary Testleri
- Minimum karakter testleri
- Maksimum karakter testleri

---

## 🧠 Veri Yönetimi

Test verileri:

- Default data object üzerinden oluşturulmaktadır.
- Step seviyesinde override edilerek negatif senaryolar uygulanmaktadır.
- Parametrik yapı desteklemektedir.

Örnek:

```java
data.password = "12345"; // override

⚙️ Çalıştırma

Projeyi klonladıktan sonra:
mvn clean test
veya TestNG Runner üzerinden çalıştırabilirsiniz.
🔐 GitHub Authentication

Push işlemleri için GitHub Personal Access Token (PAT) kullanılmaktadır.
🎯 Amaç

Bu proje ile:
    •    UI validation kontrollerinin doğrulanması
    •    Backend validation açıklarının tespiti
    •    Negatif test stratejisinin uygulanması
    •    Test otomasyon mimarisinin kurulması

hedeflenmiştir.

👨‍💻 Geliştirici

Hasan Hüseyin Kılıç
QA Automation Engineer
