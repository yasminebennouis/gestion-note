package com.gestionnotes.util;

import com.gestionnotes.model.Etudiant;
import com.gestionnotes.model.Note;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExcelHelper {

    // === IMPORT ETUDIANTS ===
    public static List<Etudiant> importerEtudiants(File file, int promotionId) {
        List<Etudiant> liste = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // sauter l'entête

                String nom = getCellValueAsString(row.getCell(0));
                String prenom = getCellValueAsString(row.getCell(1));
                String email = getCellValueAsString(row.getCell(2));

                // Sécurisation de la date
                Cell dateCell = row.getCell(3);
                LocalDate naissance = null;
                if (dateCell != null && DateUtil.isCellDateFormatted(dateCell)) {
                    naissance = dateCell.getDateCellValue().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                } else {
                    throw new RuntimeException("Date de naissance invalide sur la ligne " + (row.getRowNum() + 1));
                }

                Etudiant e = new Etudiant(nom, prenom, email, naissance, promotionId);
                liste.add(e);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erreur import étudiants : " + e.getMessage(), e);
        }
        return liste;
    }

    // === IMPORT NOTES ===
    public static List<Note> importerNotes(File file) {
        List<Note> liste = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;

                double valeur = row.getCell(0).getNumericCellValue();
                int etudiantId = (int) row.getCell(1).getNumericCellValue();
                int sousModuleId = (int) row.getCell(2).getNumericCellValue();

                Cell dateCell = row.getCell(3);
                LocalDate date = null;
                if (dateCell != null && DateUtil.isCellDateFormatted(dateCell)) {
                    date = dateCell.getDateCellValue().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                } else {
                    throw new RuntimeException("Date invalide sur la ligne " + (row.getRowNum() + 1));
                }

                Note note = new Note(valeur, etudiantId, sousModuleId, date);
                liste.add(note);
            }

        } catch (Exception e) {
            throw new RuntimeException("Erreur import notes : " + e.getMessage(), e);
        }
        return liste;
    }

    // Méthode utilitaire pour convertir proprement les cellules texte
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue();
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf((int) cell.getNumericCellValue());
        return cell.toString();
    }
}