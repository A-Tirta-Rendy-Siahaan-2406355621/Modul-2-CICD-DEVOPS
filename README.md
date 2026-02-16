REFLEKSI 1:

Repository ini berisi implementasi aplikasi manajemen produk sederhana menggunakan Spring Boot yang saya kerjakan pada exercise ini. Saya mengimplementasikan fitur untuk menampilkan daftar produk dan menambahkan produk baru. Pengembangan aplikasi dilakukan secara bertahap agar alur pengerjaan jelas dan setiap fitur dapat diuji dengan baik sebelum melanjutkan ke tahap berikutnya.

Dalam mengembangkan aplikasi ini, saya membagi struktur kode ke dalam beberapa layer, yaitu model, repository, service, dan controller. Model digunakan untuk merepresentasikan data produk, repository bertugas mengelola data produk, service menangani logika bisnis, dan controller berfungsi sebagai penghubung antara user dan sistem. Pembagian ini saya terapkan untuk menjaga agar setiap bagian kode memiliki tanggung jawab yang jelas dan tidak saling bercampur.

Saya berusaha menerapkan prinsip clean code dengan memberikan penamaan class dan method yang jelas dan sesuai dengan fungsinya, seperti Product, ProductService, create, dan findAll. Selain itu, saya menggunakan interface pada service agar kode lebih fleksibel dan tidak bergantung langsung pada implementasinya. Setiap method saya buat sesederhana mungkin dan hanya fokus pada satu tugas utama sehingga lebih mudah dipahami dan dirawat.

Dari sisi secure coding, saya tidak menyimpan data sensitif di dalam source code. Proses input data dari user dilakukan melalui mekanisme model binding yang terstruktur, sehingga alur data lebih terkontrol. Interaksi user dengan aplikasi juga dibatasi melalui endpoint yang disediakan oleh controller untuk menghindari akses langsung ke layer lain.

Pada proses pengelolaan versi menggunakan Git, saya mengembangkan fitur utama pada branch list-product . Setelah seluruh fitur selesai dan diuji, saya menggabungkan branch tersebut ke branch utama menggunakan fast-forward merge. Karena tidak ada perubahan baru pada branch utama, Git tidak membuat merge commit khusus dan hanya memajukan pointer branch ke commit terbaru.

Secara keseluruhan, melalui exercise ini saya menjadi lebih memahami pentingnya penulisan kode yang rapi, terstruktur, dan aman sejak awal pengembangan. Ke depannya, saya masih dapat meningkatkan kualitas aplikasi dengan menambahkan validasi input, memperbaiki mekanisme dependency injection, serta menambahkan penanganan error agar aplikasi menjadi lebih robust.


REFLEKSI 2:


Setelah menulis unit test, saya merasa lebih yakin terhadap perilaku kode yang saya buat karena setiap fungsi penting sudah diuji secara terpisah. Dalam satu class, jumlah unit test tidak ditentukan secara pasti, tetapi idealnya setiap public method dan setiap skenario penting (positif dan negatif) memiliki minimal satu test. Untuk memastikan unit test sudah cukup, kita bisa menggunakan code coverage sebagai metrik bantuan untuk melihat bagian mana dari kode yang sudah diuji. Namun, meskipun code coverage mencapai 100%, hal tersebut tidak menjamin kode bebas dari bug, karena unit test hanya menguji skenario yang kita pikirkan, bukan semua kemungkinan kesalahan logika atau edge case yang mungkin terjadi.

Pada bagian functional test, ketika diminta membuat test baru untuk memverifikasi jumlah item di product list, menyalin ulang setup yang sama (seperti inisialisasi WebDriver, base URL, dan konfigurasi Spring Boot) ke class baru dapat membuat kode menjadi kurang bersih. Duplikasi kode ini berpotensi menurunkan kualitas kode karena menyulitkan perawatan dan meningkatkan risiko inkonsistensi jika ada perubahan di masa depan. Hal ini melanggar prinsip DRY (Don’t Repeat Yourself).

Untuk meningkatkan kebersihan kode, setup bersama seperti inisialisasi base URL dan konfigurasi Selenium sebaiknya dipisahkan ke dalam base test class yang dapat diwarisi oleh semua functional test suite. Dengan begitu, setiap test class hanya fokus pada skenario pengujian masing-masing, kode menjadi lebih rapi, mudah dibaca, dan lebih mudah dirawat apabila terjadi perubahan konfigurasi di kemudian hari.


REFLEKSI 3:

1. Selama mengerjakan exercise, saya menemukan beberapa code quality issue yang terdeteksi oleh PMD dan pipeline CI.

Pertama, saya memperbaiki issue AvoidDuplicateLiterals. String literal yang sama (misalnya "/product/list") muncul beberapa kali di dalam file test. Untuk memperbaikinya, saya membuat sebuah konstanta seperti private static final String PRODUCT_LIST_URL = "/product/list"; lalu mengganti semua penggunaan string yang sama dengan konstanta tersebut. Strategi ini mengurangi duplikasi kode dan meningkatkan maintainability, karena jika URL berubah, cukup diubah di satu tempat saja.

Kedua, saya memperbaiki issue UseLocaleWithCaseConversions. Terdapat pemanggilan toLowerCase() atau toUpperCase() tanpa menyertakan Locale. Saya menggantinya dengan toLowerCase(Locale.ROOT) atau toUpperCase(Locale.ROOT) agar hasil konversi huruf konsisten dan tidak bergantung pada pengaturan locale sistem. Ini membuat aplikasi lebih aman dan stabil di berbagai environment.

Ketiga, saya memperbaiki issue AvoidAccessibilityAlteration pada class test. Sebelumnya, saya menggunakan reflection dengan setAccessible(true) untuk meng-inject dependency ke ProductServiceImpl. Cara ini melanggar prinsip encapsulation dan dianggap tidak baik oleh static analysis tool. Untuk memperbaikinya, saya mengubah pendekatan menjadi constructor injection sehingga dependency dapat diberikan melalui constructor tanpa perlu mengubah visibility field. Strategi ini membuat kode lebih bersih, lebih testable, dan sesuai dengan praktik yang direkomendasikan dalam pengembangan aplikasi berbasis Spring.

2. Menurut saya, workflow yang saya buat sudah memenuhi Continuous Integration (CI) karena setiap ada push ke branch (atau minimal ke branch utama), pipeline otomatis menjalankan build dan test suite, serta menjalankan analisis kualitas kode (PMD/CodeQL). Ini memastikan perubahan kecil yang sering di-commit langsung divalidasi secara otomatis sehingga error cepat terdeteksi sebelum merge/rilis. Selain itu, hasil test dan quality check menjadi “gate” yang membantu menjaga kualitas kode secara konsisten.

Untuk Continuous Deployment (CD), implementasi saya sudah mengarah ke CD karena setelah pipeline sukses, proses build & push Docker image ke registry berjalan otomatis dan service di PaaS (Koyeb) dapat menggunakan image terbaru untuk deploy. Namun, tingkat “continuous”-nya tergantung apakah Koyeb benar-benar auto-pull image terbaru setiap ada update tag (misalnya latest) tanpa langkah manual. Jika masih butuh update tag/trigger manual di Koyeb, maka itu lebih tepat disebut Continuous Delivery (siap deploy setiap saat) daripada full Continuous Deployment (langsung deploy otomatis tanpa intervensi).

Link Koyeb : https://aesthetic-jada-a-2406355621-tirtarendysiahaan-46703dbc.koyeb.app/



