package com.upadhya.textbook.api;

import com.upadhya.PostgresIntegrationTest;
import com.upadhya.textbook.infrastructure.TextbookRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.ByteArrayOutputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
class TextbookControllerTest extends PostgresIntegrationTest {
    @Autowired MockMvc mockMvc;
    @Autowired TextbookRepository repository;

    @BeforeEach void clearDatabase() { repository.deleteAll(); }

    @Test
    void uploadsPdfAndPersistsMetadata() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "math.pdf", "application/pdf", pdf(2));
        mockMvc.perform(validRequest(file))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Standard 3 Mathematics Term 1"))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.status").value("UPLOADED"));
        org.assertj.core.api.Assertions.assertThat(repository.count()).isEqualTo(1);
    }

    @Test
    void rejectsNonPdfFile() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "notes.txt", "text/plain", "not a pdf".getBytes());
        mockMvc.perform(validRequest(file))
                .andExpect(status().isUnsupportedMediaType())
                .andExpect(jsonPath("$.message").value("Only PDF files are supported"));
    }

    @Test
    void rejectsMissingRequiredMetadata() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "math.pdf", "application/pdf", pdf(1));
        mockMvc.perform(multipart("/api/v1/textbooks").file(file).param("grade", "3"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.title").exists())
                .andExpect(jsonPath("$.validationErrors.board").exists());
    }

    private org.springframework.test.web.servlet.RequestBuilder validRequest(MockMultipartFile file) {
        return multipart("/api/v1/textbooks").file(file)
                .param("title", "Standard 3 Mathematics Term 1")
                .param("board", "Tamil Nadu State Board").param("grade", "3")
                .param("subject", "Mathematics").param("term", "Term 1")
                .param("language", "English").param("edition", "2025");
    }

    private byte[] pdf(int pages) throws Exception {
        try (PDDocument document = new PDDocument(); ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            for (int i = 0; i < pages; i++) document.addPage(new PDPage());
            document.save(output);
            return output.toByteArray();
        }
    }
}
