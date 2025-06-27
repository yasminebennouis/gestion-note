package com.gestionnotes.controller;

import com.gestionnotes.model.Note;
import com.gestionnotes.service.NoteService;

import java.util.List;

public class NoteController {

    private final NoteService service;

    public NoteController(NoteService service) {
        this.service = service;
    }

    public List<Note> getAll() {
        return service.getToutesLesNotes();
    }

    public void ajouter(Note note) {
        service.ajouterNote(note);
    }

    public void supprimer(Note note) {
        service.supprimerNote(note.getId());
    }
}
