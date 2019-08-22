package dropbox;

/**
 *  input "/foo"
 * - /foo
 *   - /images
 *     - /foo.png  <------|
 *   - /temp                   | same file contents
 *     - /baz                   |
 *       - /that.foo  <- - - |-- |
 *   - /bar.png  <------- |     |
 *   - /file.tmp  <------------| same file contents
 *   - /other.temp  <--------|
 *   - /blah.txt
 *
 *
 * Output:
 * [
 *    ['/foo/bar.png', '/foo/images/foo.png'],
 *    ['/foo/file.tmp', '/foo/other.temp', '/foo/temp/baz/that.foo']
 * ]
 */
public class FindDuplicate {
}
