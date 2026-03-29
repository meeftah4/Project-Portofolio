package com.belajar.belajarspringboot; // Atau ganti ke nama package controller yang Anda buat

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.belajar.belajarspringboot.model.Pengguna;
import com.belajar.belajarspringboot.repository.PenggunaRepository;

import java.util.List;

@RestController
public class PenggunaController {

    // Spring akan otomatis "menyuntikkan" / "meminjamkan" objek Repository-nya
    @Autowired
    private PenggunaRepository penggunaRepository;

    // 1. ENDPOINT UNTUK MENYIMPAN DATA (INSERT)
    // Coba akses URL ini di browser nanti: http://localhost:8080/tambah-user?nama=Budi&email=budi@email.com
    @GetMapping("/tambah-user")
    public String simpanDataPengguna(@RequestParam String nama, @RequestParam String email) {
        
        // Membungkus data nama dan email yang dilempar dari URL ke dalam objek/model Pengguna
        Pengguna penggunaBaru = new Pengguna(nama, email);
        
        // Perintah INTI ke Database (setara dengan: INSERT INTO pengguna...)
        penggunaRepository.save(penggunaBaru);
        
        return "Berhasil menyimpan user: " + nama + " dengan email: " + email;
    }

    // 2. ENDPOINT UNTUK MENAMPILKAN SEMUA DATA (SELECT *)
    // Coba akses URL ini: http://localhost:8080/semua-user
    @GetMapping("/semua-user")
    public List<Pengguna> tampilkanSemuaData() {
        
        // Perintah INTI ke Database (setara dengan: SELECT * FROM pengguna)
        return penggunaRepository.findAll();
    }
}
