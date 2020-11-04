package com.example.cloud.controller;

import com.example.cloud.dao.DocumentRepository;
import com.example.cloud.dao.TypeRepository;
import com.example.cloud.dao.UserRepository;
import com.example.cloud.domain.Document;
import com.example.cloud.domain.Type;
import com.example.cloud.domain.User;
import com.example.cloud.util.FileTypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * @author TeqGin
 */
@Controller
@RequestMapping("/file")
@EnableAutoConfiguration
public class FileController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private DocumentRepository documentRepository;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @GetMapping("/index")
    public String index(Model model,HttpServletRequest request){
        User user = (User)request.getSession().getAttribute("user");
        model.addAttribute("user", user);
        return "/fileload/upload";
    }


    /**
     *
     * @author TeqGin
     * @param request
     * @param doc 需要上传到服务器的文件
     * @return
     */
    @PostMapping(value = "/upload",consumes = "multipart/form-data")
    public String upload(HttpServletRequest request,
                         @RequestParam("doc") MultipartFile doc){
        String path = "";
        String fileName = "";
        User user = (User)request.getSession().getAttribute("user");
        String suffixName = "";
        long fileSize = 0;
        String fileSizeValue = "";

        try {
            if (doc.isEmpty()){
                path = null;
                fileName = null;
            }else {
                //get file name
                fileName = doc.getOriginalFilename();
                String pre = File.separator + "upload/";
                path = request.getServletContext().getRealPath(pre) + fileName;
                suffixName = doc.getOriginalFilename().substring(doc.getOriginalFilename().lastIndexOf("."));

                File dest = new File(path);
                if (!dest.getParentFile().exists()){
                    dest.getParentFile().mkdir();
                }
                fileSize = dest.length();
                doc.transferTo(dest);

                String suffix = "kb";
                try(FileChannel fc = new FileInputStream(dest).getChannel()){
                    fileSize = fc.size() / 1024; //kb
                    if (fileSize > 1024){
                        fileSize = fileSize / 1024; //mb
                        suffix = "M";
                        if (fileSize > 1024){
                            fileSize /= 1024;
                            suffix = "G";
                        }
                    }
                    fileSizeValue = fileSize + suffix;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        String id = UUID.randomUUID().toString();
        String now_date = sdf.format(new Date());
        Document d = new Document();
        d.setFileName(fileName);
        d.setId(id);
        d.setPath(path);
        d.setTypeName(FileTypeUtil.getFileType(suffixName));
        d.setUperId(user.getAccount());
        d.setDate(now_date);
        d.setSize(fileSizeValue);

        documentRepository.save(d);

        return "redirect:" + "/user/index";
    }

    @PostMapping("/download")
    private void downloadFile(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam("id")String id){
        Document doc = documentRepository.getOne(id);
        //get download path
        String pre =File.separator + "upload/";
        String fileName = doc.getFileName();
        String downloadPath = request.getServletContext().getRealPath(pre) + fileName;

        File file = new File(downloadPath);
        //encode file name
        try {
            fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (file.exists()){
            //set response head
            response.setContentType("application/force-download");
            response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);

            byte []buffer =new byte[1024];

            try(FileInputStream fis = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(fis);
                OutputStream outputStream = response.getOutputStream()) {
                //write file
                int i = bis.read(buffer);
                while (i != -1){
                    outputStream.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    //删除文件
    @PostMapping("/del")
    @ResponseBody
    public void del(@RequestParam("id")String id, HttpServletRequest request){
        Document doc = documentRepository.getOne(id);
        String pre =File.separator + "upload/";
        String fileName = doc.getFileName();
        String downloadPath = request.getServletContext().getRealPath(pre) + fileName;
        File file = new File(downloadPath);
        if (file.exists()){
            file.delete();
        }
        documentRepository.delete(doc);
    }


    @GetMapping("/user_file")
    public String userFile(HttpServletRequest request, Model model){
        User user = (User) request.getSession().getAttribute("user");
        List<Document> documentList = documentRepository.findDocumentsByUserId(user.getAccount());
        String allNums = "已全部加载，共" + documentList.size() + "个文件";
        String type = "我的文件";
        List<Type> typeList = typeRepository.findAll();
        model.addAttribute("typeList", typeList);
        model.addAttribute("type", type);
        model.addAttribute("all_nums", allNums);
        model.addAttribute("documentList", documentList);
        model.addAttribute("user", user);
        return "/user/index";
    }

    @PostMapping("/search_file")
    public String searchFile(@RequestParam("key")String key, HttpServletRequest request, Model model){
        User user = (User) request.getSession().getAttribute("user");
        List<Document> documentList = documentRepository.findDocumentsLikeFileName(key);
        String allNums = "已全部加载，共" + documentList.size() + "个文件";
        String type = "查找文件";
        List<Type> typeList = typeRepository.findAll();
        model.addAttribute("typeList", typeList);
        model.addAttribute("type", type);
        model.addAttribute("all_nums", allNums);
        model.addAttribute("documentList", documentList);
        model.addAttribute("user", user);
        return "/user/index";
    }

    /**
     * @param typeName 根据类型查询
     * @return
     */
    @PostMapping("/select_file_type")
    public String selectFileType(@RequestParam("type_name")String typeName, Model model, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        List<Document> documentList = documentRepository.findDocumentsByTypeName(typeName);
        String allNums = "已全部加载，共" + documentList.size() + "个文件";
        String type = typeName;
        List<Type> typeList = typeRepository.findAll();
        model.addAttribute("typeList", typeList);
        model.addAttribute("type", type);
        model.addAttribute("all_nums", allNums);
        model.addAttribute("documentList", documentList);
        model.addAttribute("user", user);
        return "/user/index";
    }
}
