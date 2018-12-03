package com.fst.modal;

/**
 * Created by Sudhir Singh on 17,October,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class BidSubmitVO {

    EventsVO eventsVO;
    CategoryVO categoryVO;
    ChoiceVO choiceVO;
    PointsVO pointsVO;

    public EventsVO getEventsVO() {
        return eventsVO;
    }

    public void setEventsVO(EventsVO eventsVO) {
        this.eventsVO = eventsVO;
    }

    public CategoryVO getCategoryVO() {
        return categoryVO;
    }

    public void setCategoryVO(CategoryVO categoryVO) {
        this.categoryVO = categoryVO;
    }

    public ChoiceVO getChoiceVO() {
        return choiceVO;
    }

    public void setChoiceVO(ChoiceVO choiceVO) {
        this.choiceVO = choiceVO;
    }

    public PointsVO getPointsVO() {
        return pointsVO;
    }

    public void setPointsVO(PointsVO pointsVO) {
        this.pointsVO = pointsVO;
    }
}
