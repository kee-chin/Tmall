package com.chinkee.tmall.pojo;

public class ProductImage {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column productimage.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column productimage.pid
     *
     * @mbg.generated
     */
    private Integer pid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column productimage.type
     *
     * @mbg.generated
     */
    private String type;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column productimage.id
     *
     * @return the value of productimage.id
     *
     * @mbg.generated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column productimage.id
     *
     * @param id the value for productimage.id
     *
     * @mbg.generated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column productimage.pid
     *
     * @return the value of productimage.pid
     *
     * @mbg.generated
     */
    public Integer getPid() {
        return pid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column productimage.pid
     *
     * @param pid the value for productimage.pid
     *
     * @mbg.generated
     */
    public void setPid(Integer pid) {
        this.pid = pid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column productimage.type
     *
     * @return the value of productimage.type
     *
     * @mbg.generated
     */
    public String getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column productimage.type
     *
     * @param type the value for productimage.type
     *
     * @mbg.generated
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }
}