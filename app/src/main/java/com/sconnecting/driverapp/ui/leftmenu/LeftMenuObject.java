package com.sconnecting.driverapp.ui.leftmenu;

/**
 * Created by TrungDao on 8/16/16.
 */

public class LeftMenuObject {

    public boolean isGroup = false;

    public String title;
    public String leftIcon;
    public String righIcon;
    public Integer section;
    public Integer index;

    public Integer sectionSize;

    public LeftMenuObject(Boolean _isGroup,Integer _section, Integer _sectionSize, String _title, String _icoLeft, String _icoRight, Integer _index){

        this.isGroup = _isGroup;
        this.section = _section;
        this.sectionSize = _sectionSize;
        this.title = _title;
        this.leftIcon = _icoLeft;
        this.righIcon = _icoRight;
        this.index = _index;

    }

    public Boolean isLastItemInSection(){
        return index == sectionSize - 1;
    }

    public Boolean isFirstItemInSection(){
        return index == 0;
    }

}
