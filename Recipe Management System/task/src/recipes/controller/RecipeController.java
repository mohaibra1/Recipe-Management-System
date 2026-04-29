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

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody RecipeDTO recipeDTO) {
        recipeService.update(id, recipeDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search/")
    public ResponseEntity<List<RecipeDTO>> search(@RequestParam(required = false) String category,
                                                  @RequestParam(required = false) String name){
        // Check for "0 parameters or more than 1" constraint
        if ((category == null && name == null) || (category != null && name != null)) {
            return ResponseEntity.badRequest().build();
        }
        if (category != null) {
            return ResponseEntity.ok(recipeService.findByCategory(category));
        } else {
            return ResponseEntity.ok(recipeService.findByName(name));
        }
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
    public ResponseEntity<Void> delete(@PathVariable Long id) {
       RecipeDTO recipeDTO = recipeService.findById(id);

        if(recipeDTO ==  null){
            return ResponseEntity.notFound().build();
        }
        recipeService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
