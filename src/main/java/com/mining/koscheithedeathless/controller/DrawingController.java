package com.mining.koscheithedeathless.controller;

import com.mining.koscheithedeathless.CrossSectionParams;
import com.mining.koscheithedeathless.model.excavation.DrawingModel;
import com.mining.koscheithedeathless.model.excavation.RenderOptions;
import com.mining.koscheithedeathless.render.SvgRenderer;
import com.mining.koscheithedeathless.service.CrossSectionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/drawing")
public class DrawingController {

    private final CrossSectionService service;
    private final SvgRenderer renderer;

    public DrawingController(CrossSectionService service, SvgRenderer renderer) {
        this.service = service;
        this.renderer = renderer;
    }

    @PostMapping(value = "/svg", produces = "image/svg+xml")
    public ResponseEntity<String> svg(@RequestBody CrossSectionParams params) {
        DrawingModel model = service.build(params.width(), params.wallHeight());
        RenderOptions opts = RenderOptions.fitTo(model, 800, 600);
        String svg = renderer.render(model, opts);
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("image/svg+xml"))
                .body(svg);
    }

    /** Сценарий B: JSON с примитивами — уже работает, готов к Maker.js/LeaferJS */
    @GetMapping(value = "/geometry", produces = MediaType.APPLICATION_JSON_VALUE)
    public DrawingModel geometry(
            @RequestParam double width,
            @RequestParam double wallHeight
    ) {
        return service.build(width, wallHeight);
    }
}
