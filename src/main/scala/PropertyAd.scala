
/** Defining all csv file objects/columns and their data types for data processing
 *
 * @param ad_id
 * @param property_id
 * @param project_name
 * @param developer
 * @param city
 * @param district
 * @param address
 * @param full_address
 * @param property_type
 * @param status
 * @param size
 * @param size_range
 * @param number_of_rooms
 * @param floor
 * @param price
 * @param price_per_sqm
 * @param price_range
 * @param project_link
 * @param apartment_link
 * @param date
 */
final case class PropertyAd(ad_id: Int,
                            property_id: String,
                            project_name: String,
                            developer: String,
                            city: String,
                            district: String,
                            address: String,
                            full_address: String,
                            property_type: String,
                            status: String,
                            size: Double,
                            size_range: String,
                            number_of_rooms: Int,
                            floor: Int,
                            price: Double,
                            price_per_sqm: Double,
                            price_range: String,
                            project_link: String,
                            apartment_link: String,
                            date: String)
