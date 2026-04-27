package recipes.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import recipes.dto.RecipeDTO;
import recipes.model.Recipe;
import recipes.repository.RecipeRepository;
import recipes.service.RecipeService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }


    @PostMapping("/new")
    public ResponseEntity<Map<String,Long>> save(@RequestBody RecipeDTO recipeDTO) {
        Long id = recipeService.save(recipeDTO);
        return ResponseEntity.ok(Map.of("id",id));
    }

//    @GetMapping
//    public ResponseEntity<List<RecipeDTO>> findAll() {
//        return ResponseEntity.ok().body(recipeService.findAll());
//    }
    @GetMapping("/{id}")
    public ResponseEntity<RecipeDTO> findById(@PathVariable Long id) {
        RecipeDTO recipeDTO = recipeService.findById(id);
        if(recipeDTO.getName() == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(recipeDTO);
    }
}
