package com.shopaway.resources;

import com.codahale.metrics.annotation.Timed;
import com.shopaway.pojo.Image;
import com.shopaway.pojo.Product;
import com.shopaway.service.ProductService;
import util.PostgresConn;

import javax.imageio.ImageIO;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
public class ProductController {
    private final PostgresConn postgresConn;
    private final AtomicLong counter;

    public ProductController(PostgresConn postgresConn ) {
        this.postgresConn = postgresConn;
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public List<Product> getProduct(@QueryParam("category") String category) {
        Connection conn = postgresConn.getConnection();

        System.out.println(category);

        ProductService productService = new ProductService(conn);
        List<Product> products = productService.queryProductsFromCategory(category);
        System.out.println(products);

        postgresConn.commit();
        postgresConn.closeConnection();
        return products;
    }


    @GET
    @Timed
    @Path("/image")
    @Produces("image/png")
    public Response getProductImage(@QueryParam("id") String id) throws FileNotFoundException {
        Connection conn = postgresConn.getConnection();
        System.out.println(id);
        ProductService productService = new ProductService(conn);
        Product product = productService.getProductOfImage(id);
        Image image = productService.getImage(id);

        postgresConn.commit();
        postgresConn.closeConnection();
        final File imgsFolder = Paths.get("assets/images/" + product.getName() + "/" + image.getName()).toFile();

        if(imgsFolder.exists()) {
            BufferedImage imageBuffer = null;
            try {
                imageBuffer = ImageIO.read(new File(Paths.get("assets/images/" + product.getName() + "/" + image.getName()).toString()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(imageBuffer, "png", baos);
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] imageData = baos.toByteArray();
            return Response.ok(new ByteArrayInputStream(imageData)).build();
        }else {
            throw new FileNotFoundException();
        }



    }

}

