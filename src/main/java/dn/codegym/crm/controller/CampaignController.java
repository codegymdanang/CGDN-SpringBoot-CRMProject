package dn.codegym.crm.controller;

import dn.codegym.crm.constants.AppConsts;
import dn.codegym.crm.dto.CampaignDTO;
import dn.codegym.crm.dto.LeadDTO;
import dn.codegym.crm.entity.Campaign;
import dn.codegym.crm.entity.Lead;
import dn.codegym.crm.repository.CampaignRepository;
import dn.codegym.crm.service.CampaignService;
import dn.codegym.crm.service.LeadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("campaigns")
public class CampaignController {
    @Autowired
    private CampaignService campaignService;

    @Autowired
    private LeadService leadService;

    @Autowired
    private CampaignRepository campaignRepository;


    @GetMapping("/list")
    public ModelAndView listCampaign() {
        ModelAndView modelAndView = new ModelAndView("campaign/list");
        List<Campaign> campaigns = campaignService.findAllByDeletedIsFalse();
        for (int i=0;i<campaigns.size();i++) {
            if (campaigns.get(i).getId().equals(AppConsts.CAMPAIGN_NULL)) {
               campaigns.remove(campaigns.get(i));
            }
        }
        modelAndView.addObject("campaigns", campaigns);
        return modelAndView;
    }

    @GetMapping("/view/{id}")
    public ModelAndView viewCampaign(@PathVariable String id) {
        CampaignDTO campaignDTO = campaignService.findById(id);
        if (campaignDTO != null) {
            ModelAndView modelAndView = new ModelAndView("campaign/view");
            modelAndView.addObject("campaign", campaignDTO);
            return modelAndView;
        } else {
            return new ModelAndView("error404");
        }
    }

    @GetMapping("/create")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("campaign/create");
        modelAndView.addObject("campaign", new CampaignDTO());
        return modelAndView;
    }

    @PostMapping("/create")
    public String saveCampaign(@ModelAttribute("campaign") CampaignDTO campaign, RedirectAttributes redirect) {
        campaignService.create(campaign);
        redirect.addFlashAttribute("message", "New campaign created successfully!");
        return "redirect:/campaigns/list";
    }

    @GetMapping("/searchName")
    public ModelAndView findAllByName(@ModelAttribute("name") String name) {
        return new ModelAndView("campaign/list", "campaigns", campaignService.searchName(name));
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditForm(@PathVariable String id) {
        CampaignDTO campaignDTO = campaignService.findById(id);
        if (campaignDTO != null) {
            ModelAndView modelAndView = new ModelAndView("campaign/edit");
            modelAndView.addObject("campaign", campaignDTO);
            return modelAndView;
        } else {
            return new ModelAndView("error404");
        }
    }

    @PostMapping("/edit")
    public String updateCourse(@ModelAttribute("campaign") CampaignDTO campaignDTO, RedirectAttributes redirect) {
        campaignService.update(campaignDTO);
        redirect.addFlashAttribute("message", "Campaign updated successfully!");
        return "redirect:/campaigns/list";
    }

    @GetMapping("/delete/{id}")
    public ModelAndView showDeleteForm(@PathVariable String id) {
        CampaignDTO campaignDTO = campaignService.findById(id);
        if (campaignDTO != null) {
            ModelAndView modelAndView = new ModelAndView("campaign/delete");
            modelAndView.addObject("campaign", campaignDTO);
            return modelAndView;
        } else {
            return new ModelAndView("error404");
        }
    }

    @PostMapping("/delete")
    public String deleteCourse(@ModelAttribute("campaign") CampaignDTO campaignDTO, RedirectAttributes redirect) {
        campaignService.delete(campaignDTO.getId());
        redirect.addFlashAttribute("message", "Campaign deleted successfully!");
        return "redirect:/campaigns/list";
    }

    @GetMapping("/addLead/{campaignId}")
    public ModelAndView viewAddLeads(@PathVariable String campaignId, Model model) {
        List<Lead> leads = leadService.findAllByCampaignId(AppConsts.CAMPAIGN_NULL);
        model.addAttribute("campaign", campaignService.findById(campaignId));
        return new ModelAndView("campaign/addLeads", "leads", leads);
    }

    @GetMapping("/addLeads/{campaignId}/{leadId}")
    public String addLeads(@PathVariable("leadId") String leadId, @PathVariable("campaignId") String campaignId, RedirectAttributes redirect) {
        LeadDTO leadDTO = leadService.findById(leadId);
        CampaignDTO campaignDTO = campaignService.findById(campaignId);
        System.out.println(campaignDTO.getName());
        leadDTO.setCampaign(campaignRepository.findById(campaignDTO.getId()).orElse(null));
        leadService.update(leadDTO);
        redirect.addFlashAttribute("message", "Add lead into campaign successfully!");
        return "redirect:/campaigns/addLead/{campaignId}";
    }

    @GetMapping("/viewLeads/{campaignId}")
    public ModelAndView viewLeadsOfCampaign(@PathVariable String campaignId, Model model) {
        List<Lead> leads = leadService.findAllByCampaignId(campaignId);
        model.addAttribute("campaign", campaignService.findById(campaignId));
        return new ModelAndView("campaign/viewLeads", "leads", leads);
    }
}