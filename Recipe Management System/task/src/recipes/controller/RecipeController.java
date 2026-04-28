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
import java.util.Optional;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }


    @PostMapping("/new")
    public ResponseEntity<Map<String,Long>> save(@Valid @RequestBody RecipeDTO recipeDTO) {
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
        if(recipeDTO == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(recipeDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
       RecipeDTO recipeDTO = recipeService.findById(id);

        if(recipeDTO ==  null){
            return ResponseEntity.notFound().build();
        }
        recipeService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
