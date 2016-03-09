package controllers;

import models.Post;
import play.Play;
import play.cache.Cache;
import play.data.validation.Required;
import play.libs.Codec;
import play.libs.F;
import play.libs.Images;
import play.libs.WS;
import play.mvc.Before;
import play.mvc.Controller;

import java.util.List;

public class Application extends Controller {

    public static void index() {
        Post frontPost = Post.find("order by postedAt desc").first();
        List<Post> olderPosts = Post.find("order by postedAt desc").from(1).fetch(10);
        render(frontPost, olderPosts);
    }

    public static void show(Long id) {
        Post post = Post.findById(id);
        String randomID = Codec.UUID();
        render(post, randomID);
    }

    public static  void Promise(){
        for (int i=0;i<10;i++){
            System.out.println(i);
            await("1s");
        }
        renderText("Loop finished");
    }

    public static void remoteData(){
        F.Promise<WS.HttpResponse> r1 = WS.url("http://sportsapi.1win88.net:8080/SportsApi.aspx?method=checkbalance&PartnerKey=635105483988109&UserName=SENI.linzhen368cn").getAsync();
        F.Promise<WS.HttpResponse> r2 = WS.url("http://sportsapi.1win88.net:8080/SportsApi.aspx?method=checkbalance&PartnerKey=635105483988109&UserName=SENI.linzhen368cn").getAsync();
        F.Promise<WS.HttpResponse> r3 = WS.url("http://sportsapi.1win88.net:8080/SportsApi.aspx?method=checkbalance&PartnerKey=635105483988109&UserName=SENI.linzhen368cn").getAsync();

        F.Promise<List<WS.HttpResponse>> promises = F.Promise.waitAll(r1, r2, r3);

        // Suspend processing here, until all three remote calls are complete.
        //List<WS.HttpResponse> httpResponses = await(promises);

        //renderText(httpResponses);

        // Suspend processing here, until all three remote calls are complete.
        await(promises, new F.Action<List<WS.HttpResponse>>() {
            public void invoke(List<WS.HttpResponse> httpResponses) {
                renderText(httpResponses);
            }
        });
    }

    public static void postComment(Long postId,
                                   @Required(message = "Author is required") String author,
                                   @Required(message = "A message is required") String content,
                                   @Required(message = "Please type the code") String code,
                                   String randomID) {
        Post post = Post.findById(postId);

        //System.out.println("code:" + code + ",cache:" + Cache.get(randomID) + ",randomID:" + randomID);
        if (!Play.id.equals("test"))
            validation.equals(code, Cache.get(randomID)).message("Invalid code. Please type it again");

        if (validation.hasErrors()) {
            render("Application/show.html", post);
        }

        post.addComment(author, content);
        flash.success("Thanks for posting %s", author);
        Cache.delete(randomID);
        show(postId);
    }

    public static void captcha(String id) {
        Images.Captcha captcha = Images.captcha();
        String code = captcha.getText("#E4EAFD");
        //System.out.println("[captcha]id:" + id + ",code:" + code);
        Cache.set(id, code, "10mn");
        renderBinary(captcha);
    }

    public static void listTagged(String tag) {
        List<Post> posts = Post.findTaggedWith(tag);
        render(tag, posts);
    }

    public static void getJSON() {
        WS.HttpResponse res = WS.url("http://sportsapi.1win88.net:8080/SportsApi.aspx?method=checkbalance&PartnerKey=635105483988109&UserName=SENI.linzhen368cn").get();
        System.out.println(res.getStatus());
        System.out.println(res.getContentType());
        System.out.println(res.getString());
        System.out.println(res.getJson());
        System.out.println(res.getHeaders().toString());
        System.out.println(res.getQueryString().size());
        System.out.println(res.getEncoding());
        System.out.println(res.getStatusText());
        renderTemplate("Application/getJson.txt");
    }

    @Before
    static void addDefaults() {
        renderArgs.put("blogTitle", Play.configuration.getProperty("blog.title"));
        renderArgs.put("blogBaseline", Play.configuration.getProperty("blog.baseline"));
    }

}