package it.sevenbits.taskmanager.core.repository.tasks;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Model of metadata for GetTaskResponse
 */

public class PaginationInfo {

    /*
    _meta": {
    "total": 121,
    "page": 3,
    "size": 25,
    "next": "/tasks?status=done&order=desc&page=4&size=25",
    "prev": "/tasks?status=done&order=desc&page=2&size=25",
    "first": "/tasks?status=done&order=desc&page=1&size=25",
    "last": "/tasks?status=done&order=desc&page=5&size=25"
  }
     */

    @JsonProperty
    private int total;
    @JsonProperty
    private int page;
    @JsonProperty
    private int size;
    @JsonProperty
    private String next;
    @JsonProperty
    private String prev;
    @JsonProperty
    private String first;
    @JsonProperty
    private String last;


    /**
     * Default constructor
     * @param path path of result page
     * @param page number of page for pagination
     * @param size amount of tasks on one page
     * @param order order of sorting tasks in list
     * @param status status of tasks
     * @param totalSize total size of elements
     */

    public PaginationInfo(final String path, final String status, final String order, final int page, final int size, final int totalSize) {
        this.total = totalSize;
        this.page = page;
        this.size = size;
        String link = "%s?status=%s&order=%s&page=%d&size=%d";
        int lastPage = Math.max((int) Math.ceil(totalSize / (float) (size)) , 1);
        this.first = String.format(link, path, status.toLowerCase(), order, 1, size);
        this.last = String.format(link, path, status.toLowerCase(), order, lastPage, size);
        if (page < lastPage) {
            this.next = String.format(link, path, status.toLowerCase(), order, page + 1, size);
        }
        if (page > 1) {
            this.prev = String.format(link, path, status.toLowerCase(), order, page - 1, size);
        }
    }
}