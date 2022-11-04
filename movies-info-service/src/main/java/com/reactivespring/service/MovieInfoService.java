package com.reactivespring.service;

import com.reactivespring.domain.MovieInfo;
import com.reactivespring.repository.MovieInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class MovieInfoService {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    public Mono<MovieInfo> addMovieInfo(MovieInfo movieInfo) {
        return movieInfoRepository.save(movieInfo);
    }


    public Mono<MovieInfo> getMovieInfoById(String id) {
        return movieInfoRepository.findById(id);
    }

    public Flux<MovieInfo> getAllMovieInfo() {
        return movieInfoRepository.findAll();
    }

    public Mono<MovieInfo> updateMovieInfo(String id, MovieInfo updateMovieInfo) {
       return movieInfoRepository.findById(id).flatMap(movieInfo1 -> {
            movieInfo1.setCast(updateMovieInfo.getCast());
            movieInfo1.setName(updateMovieInfo.getName());
            movieInfo1.setRelease_date(updateMovieInfo.getRelease_date());
            return movieInfoRepository.save(movieInfo1);
        });
    }

    public Mono<Void> deleteMovieInfo(String id) {
        return movieInfoRepository.deleteById(id);
    }
}
