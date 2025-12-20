package data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerCsvRepository {

    private final File file;

    public CustomerCsvRepository(String filePath) {
        this.file = new File(filePath);
    }

    public List<String[]> loadAll() {
        ensureFileExists();

        List<String[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (firstLine) {
                    firstLine = false;
                    if (line.toLowerCase().startsWith("id,")) continue;
                }

                String[] parts = line.split(",", -1);
                if (parts.length < 4) continue;

                rows.add(new String[]{parts[0], parts[1], parts[2], parts[3]});
            }
        } catch (IOException e) {
            throw new RuntimeException("Gagal membaca CSV: " + file.getPath(), e);
        }
        return rows;
    }

    public void saveAll(List<String[]> rows) {
        ensureFileExists();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            bw.write("id,nama,no_hp,alamat");
            bw.newLine();

            for (String[] r : rows) {
                String id = safe(r[0]);
                String nama = safe(r[1]);
                String noHp = safe(r[2]);
                String alamat = safe(r[3]);

                bw.write(id + "," + nama + "," + noHp + "," + alamat);
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Gagal menyimpan CSV: " + file.getPath(), e);
        }
    }

    private void ensureFileExists() {
        try {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) parent.mkdirs();

            if (!file.exists()) {
                file.createNewFile();
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
                    bw.write("id,nama,no_hp,alamat");
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Gagal menyiapkan CSV: " + file.getPath(), e);
        }
    }

    private String safe(String s) {
        if (s == null) return "";
        return s.replace(",", " ").replace("\n", " ").replace("\r", " ").trim();
    }
}
