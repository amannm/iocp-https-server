/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package v2.cache;

/**
 *
 * @author admin
 */
public enum CacheDirective {
    /*
     cache-directive = cache-request-directive | cache-response-directive
     cache-request-directive =
     | "no-cache"                          ; Section 14.9.1
     | "no-store"                          ; Section 14.9.2
     | "max-age" "=" delta-seconds         ; Section 14.9.3, 14.9.4
     | "max-stale" [ "=" delta-seconds ]   ; Section 14.9.3
     | "min-fresh" "=" delta-seconds       ; Section 14.9.3
     | "no-transform"                      ; Section 14.9.5
     | "only-if-cached"                    ; Section 14.9.4
     | cache-extension                     ; Section 14.9.6
     cache-response-directive =
     | "public"                               ; Section 14.9.1
     | "private" [ "=" <"> 1#field-name <"> ] ; Section 14.9.1
     | "no-cache" [ "=" <"> 1#field-name <"> ]; Section 14.9.1
     | "no-store"                             ; Section 14.9.2
     | "no-transform"                         ; Section 14.9.5
     | "must-revalidate"                      ; Section 14.9.4
     | "proxy-revalidate"                     ; Section 14.9.4
     | "max-age" "=" delta-seconds            ; Section 14.9.3
     | "s-maxage" "=" delta-seconds           ; Section 14.9.3
     | cache-extension                        ; Section 14.9.6
     cache-extension = token [ "=" ( token | quoted-string ) ]
     */

    no_cache,
    no_store,
    no_transform,
    max_age,
    max_stale,
    s_maxage,
    min_fresh,
    only_if_cached,
    _public,
    _private,
    must_revalidate,
    proxy_revalidate,
}
