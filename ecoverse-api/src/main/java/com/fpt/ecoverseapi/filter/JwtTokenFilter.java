//package com.fpt.ecoverseapi.filter;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//@Component
//public class JwtTokenFilter extends OncePerRequestFilter {
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        String path = request.getServletPath();
//        return path.equals("/partnerships/register");
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//
//        // TODO: lấy token từ Authorization header
//        // String authHeader = request.getHeader("Authorization");
//        // if (authHeader != null && authHeader.startsWith("Bearer ")) { validate token + set Authentication }
//
//        // Quan trọng: luôn cho request đi tiếp
//        filterChain.doFilter(request, response);
//    }
//}
