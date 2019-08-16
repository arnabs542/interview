package lyft;

/**
 * Given an API to fetch page (10 results on each page):
 *
 * MAX_RESULTS = 103
 *
 * def fetch_page(page):
 *     page = page or 0 # start from page 0 if not specified
 *     return {
 *         "page": page + 1,
 *         "results": [range(10 * page, min(10 * (page + 1), MAX_RESULTS))]
 *     }
 *
 * Implement a ResultRecher class with a fetch method to return required number of results:
 * class  ResultRecher:
 *
 *     def fetch_page(num_results):
 *
 * */
public class ResultRecher {

}
