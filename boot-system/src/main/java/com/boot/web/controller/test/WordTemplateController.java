package com.boot.web.controller.test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/wordTemplate")
public class WordTemplateController {
    @Autowired
    private Configuration configuration; //freeMarker configuration
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

   @RequestMapping("/downoload")
    public void downLoad(HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateException {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("contractName","合同名称");



        List<Map<String,Object>> deviceList = new ArrayList<>();
        Map<String,Object> deviceInfo = new HashMap<>();
       deviceInfo.put("deviceName","设备名称1");
       deviceInfo.put("deviceCode","1234");
       deviceInfo.put("deviceModel","设备型号1");
       deviceInfo.put("deviceImg","iVBORw0KGgoAAAANSUhEUgAAAQQAAAEECAYAAADOCEoKAAAVGUlEQVR4Xu3d4XbcNhYD4Pj9Hzp77NNNPU5t8xMFmdRg//YKAwK4GGqadF9+//79+1f/VwWqQBX49evXSwuhOagCVeD/CrQQmoUqUAX+KNBCaBiqQBVoITQDVaAK/K1AbwhNRRWoAr0hNANVoAr0htAMVIEq8IUCfWVoPKpAFegrQzNQBapAXxmagSpQBc56ZXh5eamYBxWQPyGe1Fl4yFGVs/BQbOF991nR+VUL+g2hxhyPjxiT1Fl4yGmVs/BQbOF991nRuYVwYRrEmOQCCA+RRzkLD8UW3nefFZ1bCBemQYxJLoDwEHmUs/BQbOF991nRuYVwYRrEmOQCCA+RRzkLD8UW3nefFZ1bCBemQYxJLoDwEHmUs/BQbOF991nRuYVwYRrEmOQCCA+RRzkLD8UW3nefFZ1bCBemQYxJLoDwEHmUs/BQbOF991nRuYVwYRrEmOQCCA+RRzkLD8UW3nefFZ1bCBemQYxJLoDwEHmUs/BQbOF991nRuYVwYRrEmOQCCA+RRzkLD8UW3nefFZ2jhaBEdjRGgprUYwUewuHVa9FDsAV3x8y9LS38FQLVI/ZHl5XIjuYkjRE9VuAhHFoI4u7fs6K17mELYcKbpDFCawUewqGFIO62EObUuvBpWQJtajnGCjyEQwtB3G0hzKl14dOyBC2ER2NEj1V0vjBaX35UUo++Mky4nDRGaK3AQzj0hiDu9oYwp9aFT8sSyDeiHmEFHsKhhaAOP86L1pq73hAmvEkaI7RW4CEcWgjibm8Ic2pd+LQsgTa1HGMFHsKhhSDuthDm1LrwaVmCFkJ/VDwrmsnc9ZVhwqWkMUJrBR7CoTcEcfcJbwgapjk5P39av8WFt2AL7irLtSPnV+2U9wrZE86Suzc9fsMTKSKCmzJEF0vDBDJzSFPYKVzVWvIhnNXDVbIX1aOF8K/NyTAJthi+ynLtyLmF8B+vIy2EFsJn33x3L7EWQgvhy1ufLICGSbB3/LbdkbN62FeGDwqI6ckFSBkjnDVMgi0695XhMQ2is3qYyt0qHvZHxQ8OJ8Mk2C2ER2NED9G5hdBXhr4yhP5rO7K0S30jgh69IfSV4csMyBLIN5fgrrJcO3LuDaE3hN4Q4Bvx7iXWQmghtBBaCA8Z0JtN6rUhVb6C2x8V+6PiaQWpiyVBFWzB7Q2hN4TTFkDDJEGVBdDfEOQbTnjI+YRDUmfFVt4yL/olfenfZXjnmpiiYRJsMbyF8Lh2orN6KAuus8Jb8iG4fWV4wlcGCWoyeKvwkDMKZ52VxRXOgttCaCF8mdtk8GRhkjwEWzjrrCyucBbcFkILoYUA/9ZFl1zmZXFbCKLsxKyYou+fgi2G9zeE/obwVeQld70h9IbQG0JvCA8Z6L9leCcHtymESbB7Q3jsKdFDdNZb3sTl89tHhXdUj/4HUv71SkzRMAm2GN5Xhr4y9JXh2749NiBL20KYW0RxSAoy6aFw1lnhHdWjN4TeED4LbzJ4sjBJHoItnHW2hfBOsR1NWemGoOEbnZeQjmIemZN8KGfBPsJ99BnhLZwF9y3TvSHsf0MYDZ3OaZgUf3Q+ugDww/Ao3yNzonVUjxZCC+GzAEtIjyzB6DPRBWghPNjQG8I7OXQBUkEV3NGlOjKnehz5jJFnRA/lLNgjXI/OCG/hLLh9ZfjgHosH3y6CLYYfDeDIc8J5BO/ojOihnAX7KP+R54S3cBbcFkIL4cusaphGgn9kJroAUOpHuI8+I1pH9ehvCP0Nob8hjK5tbq6F8E5babycJb9+iSlv1yv4dhFswV1JjxQX0UN0Vg9T53vFFd5RPXpD6A2hN4Tkqo9htxB6Q/g0KfINMBa3Y1MS0mOfMPaU6KGcBXuM7bEp4S2cBbc/KvZHxf6oCK99x1Z97ClZ3NsXwphk600ljVnvtD/HqDo/ap/UY4k/mPRzUZv75KQxc8zu9XR1biFskegG9RqbqnML4ZqkTX5Kgzop4ODj1bmFMBiVnx1rUK/Rvzq3EK5J2uSnNKiTAg4+Xp1bCINR+dmxBvUa/atzC+GapE1+SoM6KeDg49W5hTAYlZ8da1Cv0b86txCuSdrkpzSokwIOPl6dWwiDUfnZsQb1Gv2rcwvhmqRNfkqDOing4OPV+QaFMOj104yl/vLKq4Ap7BRukvPTBGrwoOLhK2Ts7zIM8n2aMTFGvhGTy7Uj56cJ1OBBxcMWwqCoZ4yJMS2E41fkM7y6E4bkroVwofNiTAuhhXBWNCV3LYSzVB/AEWNaCC2EgUgNjUjuWghDkp4zJMa0EFoI56TOfnBuIZyl+gBOC+FRpKQeA3Y8zYjo3EK4MBZiTG8IvSGcFU3JXQvhLNUHcMSYFkILYSBSQyOSuxbCkKTnDIkxLYQWwjmp628IZ+l4Ok4Lob8hnB6qAUDJHd8QBj6/Iz+ggN4oRilqmEZxO7euAvRHl9c9xnMzayE8t/9nnr6FcKaaP4TVQvgh4W/4sS2EG5jaQriBiYscoYWwiBEzNFoIM+r12fcKtBBukIcWwg1MXOQILYRFjJih0UKYUa/P9oZwswy0EG5m6A8epzeEHxT/rI9uIZylZHFaCDfIQAvhBiYucoQWwiJGzNBoIcyo12cP/4aQCl7SkuQfvxU9hIfgvmqXwhZc9VDOuAoPPaPMyxmT2tENQYiIGMlZEVp5iB7CQ3BbCOra47xqPfdpnz+dyofgvrJrIUw4LGESYwS3hTBh4OsCvLzMAZz0dCofgttCmDRTwiTGCG4LYc5E1Xru03pDSOk3jCuLOAz6z6CESXgIbgtBXesrw1eK9ZVhIk+yuC2E44so2qmd4qFiy7ycUTgLbl8ZxLH/mE0ZI7i9IcyZqFrPfVpfGVL6DeNqQw4D4w9SwkNDmsIWXNHt7ZsIfsxbhYeeUebljEnt+sogrn2YTRkjuL0hTBiIxTT3SV8/3UJIqvsOW4RWSrK4wkNwWwjq2vHfMuY+qYWQ1G8YWxZxGPSfQVlc4SG4LQR1rYXwlWJ9ZZjIkyxuC+H4Iop2aqd4qNgyL2cUzoL79tvOb3hCiKTEWOkHKTljSrtVbgh6PoidyEw/Vqp2QkT1EGyZVZ1bCKLuxGwyIGK68EjhrrSIckaxX3QWXJ3V87UQVOGD88mAiOnCI4XbQjgYogOPiYd9ZTgg8NFHZBH1M8R04ZHCbSGow8fnxcMWwnGd+UlZRAUX04VHCreFoA4fnxcPWwjHdeYnZREVXEwXHincFoI6fHxePGwhHNeZn5RFVHAxXXikcFsI6vDxefGwhXBcZ35SFlHBxXThkcJtIajDx+fFwxbCcZ35SVlEBRfThUcKt4WgDh+fFw9bCMd15idlERVcTBceKdwWgjp8fF48bCEc15mflEVUcDFdeKRwWwjq8PF58ZAL4Titr5+UkCoHFUTwhfcqPOR8wlm0EA7PMpvSWnBbCJNpkyVQY4Sa8BBc4ZziIHx3nk1pLbgthMkEyRKoMUJNeAiucE5xEL47z6a0FtwWwmSCZAnUGKEmPARXOKc4CN+dZ1NaC24LYTJBsgRqjFATHoIrnFMchO/OsymtBbeFMJkgWQI1RqgJD8EVzikOwnfn2ZTWgttCmEyQLIEaI9SEh+AK5xQH4bvzbEprwW0hTCZIlkCNEWrCQ3CFc4qD8N15NqW14LYQJhMkS6DGCDXhIbjCOcVB+O48m9JacFsIkwmSJVBjhJrwEFzhnOIgfHeeTWktuC2EyQTJEqgxQk14CK5wTnEQvjvPprQWXC4EMV2J7GzmCPe7ayfnG9Hr/UyzpIodn4/9R1Zr4qMpsjA7aifn07juqIeecZX5FsJFTsjC7LgAcj6VfEc99IyrzLcQLnJCFmbHBZDzqeQ76qFnXGW+hXCRE7IwOy6AnE8l31EPPeMq8y2Ei5yQhdlxAeR8KvmOeugZV5lvIVzkhCzMjgsg51PJd9RDz7jKfAvhIidkYXZcADmfSr6jHnrGVeZbCBc5IQuz4wLI+VTyHfXQM64y30K4yAlZmB0XQM6nku+oh55xlfkWwkVOyMLsuAByPpV8Rz30jKvMUyEsQ/rlJUJFg5daglV4iMhJzootvFfxUDgnZ1sI79TV4K0SphQPCV5SO8UW3intkpzlfDrbQmghaGb+c14XQBZRseVAwkNwk5yFh862EFoImpkWwoBiLYQBkc4aWaXV785D/NIFEO0UW3gLD8FNchYeOtsbQm8ImpneEAYUayEMiHTWyCqtfnce4pcugGin2MJbeAhukrPw0NneEHpD0Mz0hjCgWAthQKSzRlZp9bvzEL90AUQ7xRbewkNwk5yFh872htAbgmamN4QBxVoIAyKdNbJKq9+dh/ilCyDaKbbwFh6Cm+QsPHS2N4TeEDQzvSEMKNZCGBCpI2MKpL61Xj99laDKGZ+B8yp6bHlDGFurfackHHrKZ1gu1WR0XnxRnZPYo+d7nWshiFoXzUo4lJIGVfFH5+WMz8B5FT1aCKMJvnBOwqG0nmG5VJPRefFFdU5ij56vNwRR6sJZCYfS0qAq/ui8nPEZOK+iR28Iowm+cE7CobSeYblUk9F58UV1TmKPnq83BFHqwlkJh9LSoCr+6Lyc8Rk4r6JHbwijCb5wTsKhtJ5huVST0XnxRXVOYo+erzcEUerCWQmH0tKgKv7ovJzxGTivokdvCKMJvnBOwqG0nmG5VJPRefFFdU5ij56vNwRR6sJZCYfS0qAq/ui8nPEZOK+iB90QhPRoMJ5lLhnqlC/PwDmlXTLXUV9+A/qO4iWNEWyQWWDfZlO+PAPnlHZsIjwQ9aWFAE5MjEZNXOT/uEbkSS2i6pziIVrorJ5R8PvKIGpNzEZNbCH8cUZ1biE8hrqFMLHk8qgGVbBToX4GzintxD+djfrSVwa149h81MTeEHpDOBbLv57qDeEkIb+DaSF8uJouUmK9IfSV4bvdjfzzFkIL4axgRbPUV4azbPoaJ2riIt+2omTqm1l1TvEQLXRWzyj4fWUQtSZmoya2EPobwkQ23z/aQjhJyO9gWgh9ZfguI6P/PJql1CtDkvSocOk5uW4m9UjxENy01ivgi4eqXQpbcF81jt0QlMgKhisHMT2pR4qH4Kp2O86Lh6pdCltwWwiTqRTT1RihluIhuMJ311nxULVLYQtuC2EymWK6GiPUUjwEV/juOiseqnYpbMFtIUwmU0xXY4RaiofgCt9dZ8VD1S6FLbgthMlkiulqjFBL8RBc4bvrrHio2qWwBbeFMJlMMV2NEWopHoIrfHedFQ9VuxS24LYQJpMppqsxQi3FQ3CF766z4qFql8IW3BbCZDLFdDVGqKV4CK7w3XVWPFTtUtiC20KYTKaYrsYItRQPwRW+u86Kh6pdCltwWwiTyRTT1RihluIhuMJ311nxULVLYQtuC2EymWK6GiPUUjwEV/juOiseqnYpbMFdphBUvFSgWDz4W4aKLWcU/YRHClfO9hZS0FmxRQ/BTnIWHnq+Jf4uw7biQVDVGDFd9BMeKVw5WwtB1XqcF797Q/igNYvXQvijoGonMZdiEtzX2RTvJGc5o56vN4R36rJ4LYQWwifb2UKY+LbdVrwWQguhhTB2YZFv2xbCmKafTYl+KV8EV08r51PsFO8kZzmjnq+vDH1l+DRfEmoNnoRaeAhuf0P4W60WQguhhaAtMjCfLLGBjz/8KtdCaCG0EGTDBmdbCP1R8cuorHKlFh4SasEd3Kk/Y8JDsVO8k5zljHq+3hB6Q+gNQTZscLaF0BvCaTcEDZN8Cwi24A7uyaFvfeEh59MfFQVbOL/ySGKLN70hLHhDkHCsFGoKXujPcOyqnfDWsiFfVvg/ahEx5HA6q0ILb8EW3BbCo8u7aie8JUu6A70h9IbwaWaiwesN4UH3FsI7OUQMbTyZ1wUQ3oItuL0h9IYgGf9utjeE3hB6Q/huS/7551LU8gXQHxU/GCBCD3p3aGwVE1UP4S3YgquCp3gI7kq3K+Ed9aU/Kv4bZRU6ZaLgrhRqKQU5o/giuCtpJ7xFD/Hk7abSQmghfBaaaPD6o2J/VPwseNKO2ngyrwsgvAVbcFf6lhOt5YzPoF1KD/GkN4QPaknwkj8ESThaCP23DLr0X833leEG/5ZBAiGlJ8UkuLuWqegs2mmpCw+dbSG0ED7NjIS6hXDdTUWXXOZbCC2EFoJszOCslGlvCB9EVfEGPeGxXb/l5KByRvFFcPvK8Ldjqp94LrO9IfSG0BuCbMzgrJRpbwi9IXwZKw3TYEbfxuSbSHgIbm8IvSH82AKklmWlUKfO2EIQZR9nRTst6uOsvn+yrwx9Zegrw/d7whMthA+SyRVSxWN3Bh8Qzr0hPIq6q3bKezBK9J9E6w2hvyH82CuULIAUteCuVKbKu4XwToFUQAR31JAjcxoO4S3YgqvnTPEQ3BZCf1T8sW9EWZhdQ506oxTTrtop71GtRbu+MoyquvicmC7BE9xkmJSH2JXUYwUecj7hm7xdvWGv8N9DUEFWmZeFkYAIbgthLg0pXwRXTyD5UB4tBHVjwd9U1PTRI0vwRjH/Pyecd+Qh51PtRA/l0UJQN1oIE4r9+6gEVRZAyaV4CK5yFj2URwtB3WghTCjWQjhDvBbCGSoGMFLGCG5/Q5gzVr5BxRfB1RMkefSGoG70hjChWG8IZ4jXQjhDxQBGyhjB7Q1hzlj5JhdfBFdPkOTRG4K60RvChGK9IZwhXgvhDBUDGCljBLc3hDlj5ZtcfBFcPUGSR28I6kZvCBOK9YZwhnhbFsIZB78ThnxjiOG7aiR6yBl31S6lh2j3Ohu7ISiRu8+L4buGWjwUPQR3V+1Seoh2LQRVa2JeDN811CKP6CG4u2qX0kO0ayGoWhPzYviuoRZ5RA/B3VW7lB6iXQtB1ZqYF8N3DbXII3oI7q7apfQQ7VoIqtbEvBi+a6hFHtFDcHfVLqWHaNdCULUm5sXwXUMt8ogegrurdik9RLsWgqo1MS+G7xpqkUf0ENxdtUvpIdq1EFStiXkxfNdQizyih+Duql1KD9GuhaBqTcyL4buGWuQRPQR3V+1Seoh2LQRVa2JeDN811CKP6CG4u2qX0kO040JQ8M5XgSqwlwL0R5f3OlrZVoEqoAq0EFSxzleBGyvQQrixuT1aFVAFWgiqWOerwI0VaCHc2NwerQqoAi0EVazzVeDGCrQQbmxuj1YFVIEWgirW+SpwYwVaCDc2t0erAqpAC0EV63wVuLECLYQbm9ujVQFV4H9koNoS+RO2fAAAAABJRU5ErkJggg==");
       deviceList.add(deviceInfo);

       deviceInfo = new HashMap<>();
       deviceInfo.put("deviceName","设备名称2");
       deviceInfo.put("deviceCode","123");
       deviceInfo.put("deviceModel","设备型号2");
       deviceInfo.put("deviceImg","iVBORw0KGgoAAAANSUhEUgAAAQQAAAEECAYAAADOCEoKAAAVa0lEQVR4Xu3d0XYjOQ4D0M7/f3T2\n" +
               "OHt6Ok53bF2pYKsc7OtQKBAkUVTFO/P2/v7+/qv/qwJVoAr8+vXrrYbQPqgCVeC3AjWE9kIVqAL/\n" +
               "KVBDaDNUgSpQQ2gPVIEq8LcC3RDaFVWgCnRDaA9UgSrQDaE9UAWqwA0FemVoe1SBKtArQ3ugClSB\n" +
               "XhnaA1WgChx1ZXh7e6uYkwrs8gvxXWooeuzCebL0Tz0mOl+I0jeEFma+tlqY+SfdPrlLDUWPXTin\n" +
               "apLEFZ1rCMlKfMHWwqSo7TJcoscunFM1SeKKzjWEZCVqCDfVlUatIcw3quhcQ5jXmU9qYfgBgwd2\n" +
               "GS7RYxfOgxJvFSY61xAeWDotTIraLsMleuzCOVWTJK7oXENIVqJXhl4ZHthf3z2qhrBBEf5FQQuT\n" +
               "SmOXt63osQvnVE2SuKJzN4RkJbohdEN4YH91Q9hAbKGgTi3YErvL21b02IWz6LxLrOjcDeGBVdPC\n" +
               "pKjtMlyixy6cUzVJ4orOUUNQIklRUtjSqKKH4KZyu+AmOaewBTepXRJb+kP1iP10WYkkBUxhpwoj\n" +
               "uKncaghJZdewpT90DmsIC7VJFUZwF+jfPSrNpJxT2IJ7V4BNA0Rr1aOGsFD0VGEEd4H+3aPSTMo5\n" +
               "hS24dwXYNEC0Vj1qCAtFTxVGcBfo3z0qzaScU9iCe1eATQNEa9WjhrBQ9FRhBHeB/t2j0kzKOYUt\n" +
               "uHcF2DRAtFY9aggLRU8VRnAX6N89Ks2knFPYgntXgE0DRGvVo4awUPRUYQR3gf7do9JMyjmFLbh3\n" +
               "Bdg0QLRWPWoIC0VPFUZwF+jfPSrNpJxT2IJ7V4BNA0Rr1aOGsFD0VGEEd4H+3aPSTMo5hS24dwXY\n" +
               "NEC0Vj1qCAtFTxVGcBfo3z0qzaScU9iCe1eATQNEa9VjC0OQBJM1YvHg30Kt2Kk8z6i1cFadBTtV\n" +
               "kwuu8BbOgnvhUUP4VGUWr4YwPSOidXQAoIbTyQ4c3EaPd2CSKozgDmg7HQJSfDxDeCv2dBJ3Dgrn\n" +
               "FIet3og1hKsyd0PohpCc+2+xxSDFxARXTT0plPCO6tEN4U+ZpSjaTIqdaj5pphSHbgh/Kyv9ITUU\n" +
               "3H5D+FIXFg/WTcVODaM0U4pDDaGGcLO3ztik3RDW7EIMUvpDcLWGaxnfPi28o3r0ytArQ7LRv8Pe\n" +
               "ZgBgy0vqtI0eNYQaQrLRawhj6tYQPukkK9CYvHNRUhRdNxV7LoP7p86otXBWnQX7vrrzEcJbOAtu\n" +
               "Pyr2o+J8By+elEaNDkCvDFeV7O8QPskhTdoNYc0RROsawrXWUT36DWG/bwhScB1LGUTBVs7CQ7AF\n" +
               "V01d9NBY4R3Vo4ZQQ9Dm/Ve8NOnl/DYD0CtDrwxHfPnWt0tqAHSYhYdg1xBErb9jpS6iteD2o+Km\n" +
               "HxWl4NqG2iCj+MpZeAi24Kqpj2oxEye8o3r0ytArw0wDfz0jTdorQzeEmz2nzXREA/8LQ1xa3y6C\n" +
               "ndRDeIjOyll4CLbgag1FD40V3lE9uiF0Q9Dm7UfFIxS7xqghfNJDHO/4UtQQjtBUa7jNAPSvDFfl\n" +
               "7w+TPskhTarrpmDrcMlACw/BVc7CQ7AFV2soemis8I7q0StDNwRt3l4ZjlCsV4ZvVRTHO74UNYQj\n" +
               "NNUabvNG7JWhV4bvBkCaVNdNxZYhlWFM8RAOl9yEh2ALrtZQaqKxwjuqR68M+20I2kzJBhnlIhxq\n" +
               "CH+rWkP4pIk202iTapwURd8uii3cRb8UD+FQQ6gh3OxvbSYZFonVYRHeii28d+AhHGoINYQawvu7\n" +
               "zDjFyjCmjEk41BBqCDWEGsJVD4gxidkIrl77yKUxWHhH9ehHxX5UxN79Z7g0aTeEbgjdELohdEO4\n" +
               "MQXdEPpXhiNezB8Y8naWxhOCwqEbQjeEbgjdELohdEMYe8fo22UM1aP07Sm8FVvY78BDOHRD6IYg\n" +
               "/X2aWBkCMQTB1eEScYWH5CccdrkSKedkfLIuW/zfn5PiJbFThRHcGsJ1hZPGlOwlwZb+UD1qCFKJ\n" +
               "L7GpwghuDaGGcKuFawgLA65HZXClMIJbQ6gh1BB0ckPxMrg1hPkipHSeZ/Tck0k9emVYqG2qMILb\n" +
               "DaEbQjeEhSE+8qgMbjeEeeVTOs8zeu7JpB7dEBZqmyqM4HZD6IbQDWFhiI88KoPbDWFe+ZTO84ye\n" +
               "ezKpRzeEhdqmCiO43RC6IXRDWBjiI4/K4HZDmFc+pfM8o+eeTOrRDWGhtqnCCG43hG4Ip9gQFubs\n" +
               "JY/usiGI2ZyR80s2z0JSUsPLY2IbwkIOL3lUCiNDqxuCYJ+R80s2z0JSUsMawoLQelQKI0NbQ9BK\n" +
               "/Kx46bsawgN7QwpTQ7gujOrxwLJu/yjpuxrCA8sphdEBSGGncJNbzQNLeopHSQ1rCA8sqRSmhtAN\n" +
               "4ajWlL6rIRyl+gCOFKaGUEMYaKmhEOm7GsKQpMcESWFqCDWEY7rO/ivbNYSjVB/AqSFci5TUY6Ac\n" +
               "PyZEdK4hPLAtpDDdELohHNWa0nc1hKNUH8CRwtQQaggDLTUUIn3HhjDEoEEPV0AMRBvk4cn0gU9V\n" +
               "gH66/FSmffi3CtQQ2hxHKVBDOErJJ+LUEJ4o/os9uobwAgWtIbxAETdJoYawSSFWaNQQVtTr2c8K\n" +
               "1BBeoB9qCC9QxE1SqCFsUogVGjWEFfV6thvCi/VADeHFCvrEdLohPFH8ox5dQzhKyeLUEF6gB2oI\n" +
               "L1DETVKoIWxSiBUaNYQV9Xp2+huCNN4ZZdaf9e6ih/IerY3kpxxS2IJ70UF4C7bgXngksUfr/cHj\n" +
               "HZgLaSGxSyxI8UF5Fz2U96jekp9ySGELbg3h706oIXzSJNnUo0M4E6e8R58hw6UcUtiCW0OoIdyc\n" +
               "hWRTjw7hTJzyHn2GDJdySGELbg2hhlBDGHUDvBLVEK6F3UUPKPf/r8H9hvBHsmQRtTASr7xHseVt\n" +
               "qxxS2ILbDaEbQjeEUTfohvCXUmI2uxgklLsbwlexkkXUwki88h7FPuMACOduCN0QuiGMukE3hG4I\n" +
               "93pF3fce3m7/XN+0u+ihvEd1l/yUQwpbcLshdEPohjDqBt0QuiHc6xV133t4u/3z5FsumavyHuUi\n" +
               "9VYOKWzB7YbwwA1BG2S0SS9xWnTBlljJUTgL7k56iHapWNUuxUPqneKghvfRS6nfISQLc0axhbNq\n" +
               "J9jJ5tsBW7VLcd6lJqpHDWGhI0RsaRDB7YZwXUDVbqH8N49KvVMcuiEklf0HtjSfNIjg1hBqCLfa\n" +
               "nnupV4Z5FxGxawjzOstJqYngaqzUW7ElXvXolUHU/RIrYkuDCG43hG4I3RDe3hbG+LijMrg1hON0\n" +
               "P3IAUqyk3ikO/YaQVLbfEB6s7tzjxKTnnjB2qoawsE6PSfwn6oxiC2dtasFWrc8Wr9ql8tulJqpH\n" +
               "vyEsdISILQ0iuP2G0G8IR16hagg1hAUF9juqZprKQF4AKQ7xbwhJ4oItYicb5Iw8RGfRTrTQRlXs\n" +
               "VI6Ce9ZY2hB2SVIaRJpa8zsjD8lRtBMtaghShcfG1hAW9JYhkOFSSsJDsIWzckhip3IU3LPG1hAW\n" +
               "KidDIAOglISHYAtn5ZDETuUouGeNrSEsVE6GQAZAKQkPwRbOyiGJncpRcM8aW0NYqJwMgQyAUhIe\n" +
               "gi2clUMSO5Wj4J41toawUDkZAhkApSQ8BFs4K4ckdipHwT1rbA1hoXIyBDIASkl4CLZwVg5J7FSO\n" +
               "gnvW2BrCQuVkCGQAlJLwEGzhrByS2KkcBfessTWEhcrJEMgAKCXhIdjCWTkksVM5Cu5ZY2sIC5WT\n" +
               "IZABUErCQ7CFs3JIYqdyFNyzxpIhSNGl4Cqe8FBsiU/mKDx20UM4p2J/Qk2SOdYQFjozWRihVUP4\n" +
               "o9ZPqEkyxxqCTN6X2GRhhFYNoYYg/XIrtoawoGQNYUG80NGfUJNkjjWEhcZMFkZodUPohiD90g3h\n" +
               "KLV6ZQgpeRzsTzDpZI7dEBZ6MVkYodUNoRuC9Es3hKPU6oYQUvI42J9g0skcuyEs9GKyMEKrG0I3\n" +
               "BOmXbghHqdUNIaTkcbA/waSTOXZDWOjFZGGEVjeEbgjSL4dtCEc9dBVHBkCGVnBXczjD+aR2gi1a\n" +
               "aQ2Fh2ALruSXjqUNIU1mFD9VGMEd5XrmOGlq1U6wRcMkD8FO5SdazMTWED6pJgWfEftsZ6SpVTvB\n" +
               "Ft2SPAQ7lZ9oMRNbQ6ghfNs30tQyLJcHCrY0dpKHYKfyEy1mYmsINYQawuDk1BAGhXp0WKowgvvo\n" +
               "nJ/xPHnLqXaCLbkneQh2Kj/RYia2G0I3hG4Ig5NTQxgU6tFhqcII7qNzfsbz5C2n2gm25J7kIdip\n" +
               "/ESLmdhuCN0QuiEMTk4NYVCoR4elCiO4j875Gc+Tt5xqJ9iSe5KHYKfyEy1mYrshdEPohjA4OTWE\n" +
               "QaEeHZYqjOA+OudnPE/ecqqdYEvuSR6CncpPtJiJPeWGMJPomc5I42leqUZVzuWhlZuLV51rCHM6\n" +
               "R0/pcAkZbZBRbOVcHqPKrsWpzjWENb0jp3W4hIQ2yCi2ci6PUWXX4lTnGsKa3pHTOlxCQhtkFFs5\n" +
               "l8eosmtxqnMNYU3vyGkdLiGhDTKKrZzLY1TZtTjVuYawpnfktA6XkNAGGcVWzuUxquxanOpcQ1jT\n" +
               "O3Jah0tIaIOMYivn8hhVdi1Oda4hrOkdOa3DJSS0QUaxlXN5jCq7Fqc61xDW9I6c1uESEtogo9jK\n" +
               "uTxGlV2LU51rCGt6R07rcAkJbZBRbOVcHqPKrsWpzjWENb0jp3W4hIQ2yCi2ci6PUWXX4lRnMgQt\n" +
               "+loqr3VaCqM6J7FHqyAcLpiSo2KPclYegpvkLDw0toagik3GS4PIsFzoJLFH0xUOOoiKPcpZeQhu\n" +
               "krPw0Ngagio2GS8NUkO4Flm00/Ko1qP4Sc6jHGbiaggzqk2ckQbRJk1ij6YqHPTNrNijnJWH4CY5\n" +
               "Cw+NrSGoYpPx0iA1hG4Ik222fKyGsCzhGEAN4VonMT3Rbqwaf6KEh2AnOQsPja0hqGKT8dIg2qRJ\n" +
               "7NF0hYOu6oo9yll5CG6Ss/DQ2BqCKjYZLw1SQ+iVYbLNlo/VEJYlHAOoIfTKMNYpz42qITxI/xpC\n" +
               "DeFBrbb0mBrCknzjh2sINYTxbnleZMwQZACel/7ak+Wun9RDeEjGSc7CQ2JTWlw4iB5JHqKHcP74\n" +
               "yPoOJyRJgJX8tordRQ/hIQKesYYpLWoI/+gcEfuMzSTD8uGmb2/DR5J6CI9hwvhGFNxkbEqLGkIN\n" +
               "4W7fSvPVEO7KeUiA1EQfKDVM8hDewrlXBlF2Y4NMNZ8206KchxxPadENYeMBOKRzDgCR5ksOl/CQ\n" +
               "tJOchYfEprSoIdQQ7vahNF9yuITH3aQ+BSQ5Cw+JTWlRQ6gh3O1Dab7kcAmPu0nVEL6VSGqYqonU\n" +
               "T02s3xBU3S/xUnRpJqUlPAQ7yVl4SGxKCx2uJA/RQ2vY3yGIujWEBbUeczQ5iDJcSR6ipHDuhiDK\n" +
               "bnyFSjWfNtOinIccT2nRDWHjATikcw4AkeZLDpfwkLSTnIWHxKa0qCE80BCSRZRm0gEQ3oItuJJf\n" +
               "Mlby+1hNT/grT8lR8lOzSdZxi28IKl5KECl4sql30UN03kU74bxTDVU/zXM0vobwSSktigyuYAvu\n" +
               "aKHTcZJfchA1T9FachTcbghfqqbiadFH46XgyabeRY9R3WYaWnLUugjvFA/BndFPcpTYbgjdEKRf\n" +
               "vo3VoZWBUWxJKMVDcGsI3RBu9qw2kwxAKlaHVnJUbMkxxUNwawg1hBpC/8pw1QNJ0yOD3OHfmKRu\n" +
               "KglKrBZFeAu24Ep+yVjJL/n9RXMUrSVHwe2G0A2hG0I3hG4I302Buqm+BUbj5Q2QfMvtoseobjNv\n" +
               "OMlR6yK8UzwEd0Y/yVFi+1eGT2pp40nRBVtwpdjJWMkvaaaao2gtOQpuDaFXhqddGZJNrcOYiJf8\n" +
               "9Pky5EkeylviuyH8sA1BGlUGQJouGSv5KQ/RI8lDeUt8DaGG8G2/yABI0yVjk4MoeiR5JPWrIdQQ\n" +
               "agiDE1ZDWLjri0OK0IO1mwoTzskPY0k9JMckj6kCDRyS/AbgrkJEjyQP5S3x3RC6IXRDGJyYGkI3\n" +
               "hJutkmoQwR3s5f/C5M2V5KG8R+Mlv1HM33GiR5KH8pb4bgjdELohDE5MDaEbQjeEwWHZJSz5Zq4h\n" +
               "1BBqCLtM+iCPGsKgUN+E9crQK0OvDIMz1A2hG8KP3hAG5+QjTIblEi9vcsUW3sJDcM8a2w2hG8Ih\n" +
               "vatDK4Oo2JKQ8BDcs8bWEGoIh/SuDq0MomJLQsJDcM8aW0OoIRzSuzq0MoiKLQkJD8E9a2wNoYZw\n" +
               "SO/q0MogKrYkJDwE96yxNYQawiG9q0Mrg6jYkpDwENyzxtYQagiH9K4OrQyiYktCwkNwzxpbQ6gh\n" +
               "HNK7OrQyiIotCQkPwT1rbA2hhnBI7+rQyiAqtiQkPAT3rLE1hBrCIb2rQyuDqNiSkPAQ3LPG1hBq\n" +
               "CIf0rg6tDKJiS0LCQ3DPGltDqCEc0rs6tDKIii0JCQ/BPWvsFoZwWvFC//WhXQYgySNV8+SAix5J\n" +
               "HintLrg1hAV1Uw0iuEpfGjXJQ3mPxkt+o5i/40SPJA/lLfE1BFHrS2yqQQRX6UujJnko79F4yW8U\n" +
               "s4bwjVLSIMnCaCFT8Sk9BFdzk7okeSjv0XjJbxSzhlBDGOoVGRhpVMEdIvopaBceyns0XvIbxawh\n" +
               "1BCGekUGVxpVcIeI1hBUpn/GS12k3oeQOwik3xAWhEw1iOAqfWnUJA/lPRov+Y1idkPohjDUKzIw\n" +
               "0qiCO0S0G4LK1A1hRDFpVBmAkWfvGJPSQ3BVF6lLkofyHo2X/EYxuyF0QxjqFRkYaVTBHSLaDUFl\n" +
               "6oYwopg0qgzAyLN3jEnpIbiqi9QlyUN5j8ZLfqOY3RAO2BBU7FePl0ZNDqLwkJoo5/IQda9jU9pd\n" +
               "nhL7K8N8uq95UoqowyWKCQ/BVc7lIerWEObV2vSkDIAOl6QsPARXOZeHqFtDmFdr05MyADpckrLw\n" +
               "EFzlXB6ibg1hXq1NT8oA6HBJysJDcJVzeYi6NYR5tTY9KQOgwyUpCw/BVc7lIerWEObV2vSkDIAO\n" +
               "l6QsPARXOZeHqFtDmFdr05MyADpckrLwEFzlXB6ibg1hXq1NT8oA6HBJysJDcJVzeYi6NYR5tTY9\n" +
               "KQOgwyUpCw/BVc7lIerWEObV2vSkDIAOl6QsPARXOZeHqLupIcyn0JNVoAqcQQH66fIZEirHKlAF\n" +
               "5hWoIcxr15NV4OUUqCG8XEmbUBWYV6CGMK9dT1aBl1OghvByJW1CVWBegRrCvHY9WQVeToEawsuV\n" +
               "tAlVgXkFagjz2vVkFXg5BWoIL1fSJlQF5hWoIcxr15NV4OUUqCG8XEmbUBWYV+B/au8LEuwfbZsA\n" +
               "AAAASUVORK5CYII=");
       deviceList.add(deviceInfo);
       map.put("deviceList",deviceList);
        // 告诉浏览器用什么软件可以打开此文件(.doc)
        //若是下载.docx  则为application/vnd.openxmlformats-officedocument.wordprocessingml.document
        response.setHeader("content-Type", "application/msword");
        // 下载文件的默认名称
        response.setHeader("Content-Disposition", "attachment;filename=someName.doc");
        //*freeMarkerConfigurer.getConfiguration().setClassForTemplateLoading(getClass(), "/static/ftl/");
        //Template template = freeMarkerConfigurer.getConfiguration().getTemplate("test.ftl");
        //默认模版地址为classpath:/templates,若要你的模版不想放在默认地址，可以使用上面注释掉的代码读取模版 or 在application.properties中
        //添加配置spring.freemarker.template-loader-path=你的模版地址（例如获取根目录下所有模版，配置如下spring.freemarker.template-loader-path=classpath:/)
        Template template = configuration.getTemplate("qcode.ftl");
        template.process(map,new OutputStreamWriter(response.getOutputStream()));
    }

    /**
     * 预览与下载代码类似，如下：
     *
     */
    @RequestMapping("/welcome")
    public void welcome(HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateException {
        //response.setHeader("content-Type","text/html");
        response.setContentType("text/html;charset=utf-8");
        Template template = configuration.getTemplate("test.ftl");
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("title","员工信息表");
        map.put("name","战士");
        map.put("id","1");
        map.put("phone","1382823723");
        map.put("address","韩式工作的决定");
        List<Map<String,Object>> users = new ArrayList<>();
        Map<String,Object> map1 = new HashMap<>();
        map1.put("name","战士");
        map1.put("id","1");
        map1.put("phone","1382823723");
        map1.put("address","韩式工作的决定");
        users.add(map1);
        Map<String,Object> map2 = new HashMap<>();
        map2.put("name","战士2");
        map2.put("phone","138282372902");
        map2.put("id","2");
        map2.put("address","世界上就动手动脚活动和内部");
        users.add(map2);
        map.put("users",users);
        template.process(map,response.getWriter());
    }
}
