package norman.backendminicapstone.service;

import norman.backendminicapstone.dto.BlogDTO;
import norman.backendminicapstone.entity.BlogEntity;
import norman.backendminicapstone.exception.UserAlreadyExist;
import norman.backendminicapstone.model.BlogRequest;
import norman.backendminicapstone.repository.BlogRepository;
import norman.backendminicapstone.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlogService {

    private final BlogRepository blogRepository;
    private final ModelMapper modelMapper;
    private final DateTimeUtil dateTimeUtil;

    public List<BlogDTO> getAllBlogs() {

        // Get all data from database
        List<BlogEntity> allBlogs = blogRepository.findAll(Sort.by(Sort.Direction.ASC, "createdDate"));

        // Initialize dto
        List<BlogDTO> allBlogsDTO = new ArrayList<>();

        allBlogs.forEach(product -> {
            allBlogsDTO.add(modelMapper.map(product, BlogDTO.class));
        });

        return allBlogsDTO;
    }

    public List<BlogDTO> addBlog(BlogRequest newBlog) {

        // Save new blog to database
        blogRepository.save(BlogEntity
                .builder()
                .blogId(UUID.randomUUID())
                .blogName(newBlog.getBlogName())
                .blogAuthor(newBlog.getBlogAuthor())
                .imageLink(null)
                .description(newBlog.getDescription())
                .createdDate(dateTimeUtil.currentDate())
                .modifiedDate(dateTimeUtil.currentDate())
                .build());

        return getAllBlogs();
    }

    public List<BlogDTO> deleteBlog(UUID blogId) {

        // Get blog
        BlogEntity blog = blogRepository.findByBlogId(blogId);

        // Check if product exist
        if(blog == null) throw new UserAlreadyExist("Blog doesn't exist");

        // Delete product
        blogRepository.deleteByBlogId(blogId);

        return getAllBlogs();
    }
}

