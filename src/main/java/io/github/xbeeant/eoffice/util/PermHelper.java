package io.github.xbeeant.eoffice.util;

import io.github.xbeeant.eoffice.model.Perm;
import io.github.xbeeant.eoffice.po.MigratePerm;

import java.util.List;

/**
 * @author xiaobiao
 * @version 2022/3/11
 */
public class PermHelper {
    /**
     * 权限合并，当一个用户（用户在多个分组下），分组的权限不一致的时候，合并用户的权限
     * 合并原则：使用有权限的配置覆盖没权限的配置
     *
     * @param perms 烫发
     * @return {@link Perm}
     * @see Perm
     */
    public static Perm migrate(List<Perm> perms) {
        MigratePerm migratePerm = new MigratePerm();
        for (Perm perm : perms) {
            // download, edit, print, `view`, comment, share
            migratePerm.setEdit(perm.getEdit());
            migratePerm.setDownload(perm.getDownload());
            migratePerm.setPrint(perm.getPrint());
            migratePerm.setView(perm.getView());
            migratePerm.setComment(perm.getComment());
            migratePerm.setShare(perm.getShare());
        }
        return migratePerm;
    }
}
