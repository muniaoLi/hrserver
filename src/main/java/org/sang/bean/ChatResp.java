package org.sang.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by sang on 2018/1/29.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResp {
    private String msg;
    private String from;
}
