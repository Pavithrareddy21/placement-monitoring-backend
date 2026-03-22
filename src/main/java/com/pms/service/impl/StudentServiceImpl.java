package com.pms.service.impl;

import com.pms.entity.PlacementStatus;
import com.pms.entity.Student;
import com.pms.exception.ResourceNotFoundException;
import com.pms.repository.StudentRepository;
import com.pms.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    // ✅ CREATE
    @Override
    public Student createStudent(Student student) {

        // 🔥 HANDLE placed properly
        Boolean placed = student.getPlaced() != null ? student.getPlaced() : false;

        student.setPlaced(placed);

        if (placed) {
            student.setPlacementStatus(PlacementStatus.PLACED);
        } else {
            student.setPlacementStatus(PlacementStatus.NOT_PLACED);
        }

        student.setCreatedAt(LocalDateTime.now());

        return studentRepository.save(student);
    }

    // ✅ GET ALL
    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // ✅ GET BY ID
    @Override
    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Student not found with id: " + id));
    }

    // ✅ UPDATE (🔥 FIXED)
    @Override
    public Student updateStudent(Long id, Student updatedStudent) {

        Student existing = getStudentById(id);

        existing.setName(updatedStudent.getName());
        existing.setEmail(updatedStudent.getEmail());
        existing.setPhone(updatedStudent.getPhone());
        existing.setCollege(updatedStudent.getCollege());
        existing.setBranch(updatedStudent.getBranch());
        existing.setPassoutYear(updatedStudent.getPassoutYear());
        existing.setCgpa(updatedStudent.getCgpa());
        existing.setSkills(updatedStudent.getSkills());

        // 🔥 CRITICAL FIX
        Boolean placed = updatedStudent.getPlaced() != null ? updatedStudent.getPlaced() : false;
        existing.setPlaced(placed);

        // 🔥 ALWAYS SYNC STATUS
        if (placed) {
            existing.setPlacementStatus(PlacementStatus.PLACED);
        } else {
            existing.setPlacementStatus(PlacementStatus.NOT_PLACED);
        }

        return studentRepository.save(existing);
    }

    // ✅ DELETE
    @Override
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}