package com.pms.controller;

import com.pms.entity.Student;
import com.pms.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final StudentRepository repo;

    @GetMapping
    public Map<String, Object> getAnalytics() {

        List<Student> students = repo.findAll();

        int total = students.size();

        long placed = students.stream()
                .filter(s -> s.getPlacementStatus() != null &&
                        s.getPlacementStatus().name().equals("PLACED"))
                .count();

        long notPlaced = total - placed;

        // CGPA buckets
        Map<String, Integer> cgpaMap = new LinkedHashMap<>();
        cgpaMap.put("6-7", 0);
        cgpaMap.put("7-8", 0);
        cgpaMap.put("8-9", 0);
        cgpaMap.put("9+", 0);

        for (Student s : students) {
            if (s.getCgpa() == null) continue;

            double cgpa = s.getCgpa();

            if (cgpa < 7) cgpaMap.put("6-7", cgpaMap.get("6-7") + 1);
            else if (cgpa < 8) cgpaMap.put("7-8", cgpaMap.get("7-8") + 1);
            else if (cgpa < 9) cgpaMap.put("8-9", cgpaMap.get("8-9") + 1);
            else cgpaMap.put("9+", cgpaMap.get("9+") + 1);
        }

        Map<String, Object> response = new HashMap<>();

        response.put("total", total);
        response.put("placed", placed);
        response.put("notPlaced", notPlaced);
        response.put("cgpa", cgpaMap);

        return response;
    }
}