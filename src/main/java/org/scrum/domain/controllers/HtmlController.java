// src/main/java/org/scrum/domain/controllers/HtmlController.java
package org.scrum.domain.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.scrum.domain.models.FacturaMdl;
import org.scrum.domain.repositories.FacturaRepo;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class HtmlController {

    @Autowired
    private FacturaRepo facturaRepo;

    @GetMapping("/main")
    public String showMainPage() {
        return "main"; // map to main.html in the templates directory
    }

    @GetMapping("search")
    public String searchInvoices(
            @RequestParam(name = "clientName", required = false) String clientName,
            @RequestParam(name = "furnizorName", required = false) String furnizorName,
            @RequestParam(name = "invoiceNr", required = false) String invoiceNr,
            Model model) {

        List<FacturaMdl> invoices = facturaRepo.findByClient_NameContainingAndFurnizor_NameContainingAndInvoiceNrContaining(
                clientName, furnizorName, invoiceNr);

        model.addAttribute("invoices", invoices);
        return "search-results";
    }

    @GetMapping("/scrum/reset")
    public String resetSearchParameters(RedirectAttributes redirectAttributes) {
        // Clear any session attributes or parameters used for search
        redirectAttributes.addAttribute("clientName", "");
        redirectAttributes.addAttribute("furnizorName", "");
        redirectAttributes.addAttribute("invoiceNr", "");

        return "redirect:/main"; // Redirect to the main page
    }
}
