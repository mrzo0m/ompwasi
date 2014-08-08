package com.epam.training.web;

import com.epam.training.persistence.pojo.FullInfo;
import com.epam.training.persistence.pojo.Item;
import com.epam.training.persistence.pojo.PageRequest;
import com.epam.training.persistence.pojo.UserContext;
import com.epam.training.persistence.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by Oleg_Burshinov on 21.01.14.
 */
@Component
@Controller
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserContext userContext;

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/api/item/create", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
    @ResponseBody
    public int create(@RequestBody Item item) {
        return itemService.create(item);
    }

    @RequestMapping(value = "/api/item/{itemId}", method = RequestMethod.GET)
    @ResponseBody
    public Item getItem(@PathVariable Integer itemId) {
        return itemService.read(itemId);
    }

    @RequestMapping(value = "/api/item/list", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public List<FullInfo> getFullInfo(@RequestBody PageRequest pageRequest) {

        return itemService.getAdvancedInfoItems(pageRequest.getPageNumber(), pageRequest.getItemsCountOnPage(), pageRequest.getOrder(), pageRequest.getFilter(), pageRequest.getCategory());
    }

    @RequestMapping(value = "/api/item/mylist", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @ResponseBody
    public List<FullInfo> getFullInfoMyItems(@RequestBody PageRequest pageRequest) {
        return itemService.getFullInfoMyItems(pageRequest.getPageNumber(), pageRequest.getItemsCountOnPage(), userContext.getUser().getUserId());
    }


    @RequestMapping(value = "/api/item/update/{itemId}", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView update(@PathVariable Integer itemId) {
        Item item = itemService.read(itemId);
        ModelAndView mav = new ModelAndView("redirect:/edititem.htm");
        mav.addObject("item", item);
        mav.addObject("userContext", userContext);
        mav.setViewName("EditItem");
        return mav;
    }


    @RequestMapping(value = "/edititem.htm", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView edititemShow(@RequestParam(value = "id", required = false) Integer id, ModelAndView mav) {
        Item item;
        if (id != null) {
            item = itemService.read(id);
        } else {
            item = new Item();
            item.setSellerId(userContext.getUser().getUserId());
        }

        mav.addObject("item", item);
        mav.addObject("userContext", userContext);
        mav.setViewName("EditItem");
        return mav;
    }

    @RequestMapping(value = "/edititem.htm", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView edititemSubmit(@Valid Item item, BindingResult result, ModelAndView mav) {
        if (result.hasErrors()) {
            mav.setViewName("EditItem");
            return mav;
        }
        if(item.getSellerId() != null){
            if (item.getSellerId().equals(userContext.getUser().getUserId())) {
                if (item.getItemId() != null) {
                    itemService.update(item);
                } else {
                    itemService.create(item);
                }
            }
        }
        return new ModelAndView("redirect:/showitems.htm");
    }

    @RequestMapping(value = "/api/item/delete", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.DELETE)
    @ResponseBody
    public boolean delete(@RequestParam(value = "id", required = true) Integer id) {
        return itemService.delete(id);
    }
}
