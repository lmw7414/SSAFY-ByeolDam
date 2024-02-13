package com.ssafy.star.contour.application;

import com.ssafy.star.contour.domain.ContourEntity;
import com.ssafy.star.contour.repository.ContourRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class ContourServiceTest {

    @Autowired
    private ContourRepository contourRepository;

    @Test
    public void insert() {
        ContourEntity contour = ContourEntity.of("originUrl",
                "thumbUrl",
                "cThumbUrl",
                makeThreeDimensionList(),
                makeTwoDimensionList()
        );

        ContourEntity actual = contourRepository.save(contour);
        assertThat(actual).isNotNull();
    }

    private List<List<List<Integer>>> makeThreeDimensionList() {
        List<List<List<Integer>>> result = new ArrayList<>();
        int[][][] arr = {
                {
                    {1 , 2}, {2, 3}, {3, 4}
                },
                {
                        {10 , 20}, {20, 30}, {30, 40}
                },
                {
                        {100 , 200}, {200, 300}, {300, 400}
                },

        };

        for(int i = 0; i < arr.length; i++) {
            List<List<Integer>> list1 = new ArrayList<>();
            for(int j = 0; j<arr[i].length; j++) {
                List<Integer> list2 = new ArrayList<>();
                for(int k = 0; k < arr[i][j].length; k++) {
                    list2.add(arr[i][j][k]);
                }
                list1.add(list2);
            }
            result.add(list1);
        }
        return result;
    }

    private List<List<Integer>> makeTwoDimensionList() {
        List<List<Integer>> result = new ArrayList<>();
        int[][] arr = {
                {1 , 2}, {2, 3}, {3, 4}
        };

        for(int i = 0; i < arr.length; i++) {
            List<Integer> list1 = new ArrayList<>();
            for(int j = 0; j<arr[i].length; j++) {
                list1.add(arr[i][j]);
            }
            result.add(list1);
        }
        return result;
    }
}