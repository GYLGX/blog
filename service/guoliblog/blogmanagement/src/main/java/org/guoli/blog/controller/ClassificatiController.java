package org.guoli.blog.controller;

import org.guoli.blog.entity.Classificati;
import org.guoli.blog.service.ClassificatiService;
import org.guoli.blog.utlis.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/classificati")
public class ClassificatiController {

    @Autowired
    private ClassificatiService classificatiService;

    @GetMapping("/open")
    private R getClassificat(){
        return R.ok().data("Classificati", classificatiService.list(null));
    }
    @GetMapping
    private R getClassificatilist(){
        return R.ok().data("Classificati", classificatiService.list(null));
    }
    @PostMapping
    private R addClassificati(@RequestBody Classificati classificati){
        boolean save = classificatiService.save(classificati);
        if (save) {
            return R.ok().message("添加新标签成功");
        }else {
            return R.error().message("添加新标签失败");
        }
    }
    @PutMapping
    private R updateClassificati(@RequestBody Classificati classificati){
        boolean flag = classificatiService.updateById(classificati);
        if (flag) {
            return R.ok().message("修改成功");
        }else {
            return R.error().message("修改失败");
        }
    }
    @DeleteMapping("/{id}")
    private  R deleteClassificati(@PathVariable int id){
        boolean flag = classificatiService.removeById(id);
        if (flag) {
            return R.ok().message("删除成功");
        }else {
            return R.error().message("删除失败");
        }
    }
}
