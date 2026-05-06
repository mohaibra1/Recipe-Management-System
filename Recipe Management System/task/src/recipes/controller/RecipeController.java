package recipes.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import recipes.dto.RecipeDTO;
import recipes.model.Recipe;
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
    public ResponseEntity<Map<String, Long>> save(
            @Valid @RequestBody RecipeDTO recipeDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        Long id = recipeService.save(recipeDTO, userDetails.getUsername());
        return ResponseEntity.ok(Map.of("id", id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody RecipeDTO recipeDTO,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        Recipe recipe = recipeService.findRecipeById(id);
        if(recipe == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        if (!recipe.getAuthorEmail().equals(userDetails.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
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
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       Authentication authentication) {
       RecipeDTO recipeDTO = recipeService.findById(id);

        if(recipeDTO ==  null){
            return ResponseEntity.notFound().build();
        }
        recipeService.deleteById(id,authentication.getName());

        return ResponseEntity.noContent().build();
    }
}
