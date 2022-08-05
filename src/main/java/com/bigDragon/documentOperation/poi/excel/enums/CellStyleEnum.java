package com.bigDragon.documentOperation.poi.excel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: bigDragon
 * @create: 2022/8/2
 * @Description:
 *  poi CellStyle 枚举，方便显示
 */
@Getter
@AllArgsConstructor
public enum CellStyleEnum {
    STRING_CELL_STYLE(0),
    DATE_CELL_STYLE(1),
    INTEGER_CELL_STYLE(2),
    DOUBLE_CELL_STYLE(3),
    DATE_CELL_STYLE2(4);

    private Integer code;
}
